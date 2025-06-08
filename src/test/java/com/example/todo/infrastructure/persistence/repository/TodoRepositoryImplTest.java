package com.example.todo.infrastructure.persistence.repository;

import com.example.todo.todo.domain.repository.TodoRepository;
import com.example.todo.todo.infrastructure.persistence.entity.Todo;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

@HelidonTest
@DisplayName("TodoRepositoryImpl 統合テスト")
public class TodoRepositoryImplTest {

    @Inject
    private TodoRepository todoRepository;  // テスト対象のリポジトリインターフェース

    @PersistenceContext(unitName = "todoPU")
    private EntityManager entityManager;

    // 各テストメソッドの前に実行され、テストデータをクリーンアップ
    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.clear();
    }

    @Test
    @Transactional  // 各テストメソッドをトランザクション内で実行し、テスト後にロールバック
    @DisplayName("新しいTodoを保存するとID、バージョン、タイムスタンプがDBで自動設定される")
    void save_newTodo_shouldPersistAndSetGeneratedValues() {
        // Arrange
        Todo newTodo = new Todo("新しいDBテストタスク");

        //Act
        Todo savedTodo = todoRepository.save(newTodo);
        assertThat(savedTodo).isNotNull().satisfies(
                todo -> assertThat(todo.getId()).isNotNull()
        );
        // DBによって自動設定される値を確認するため、再取得する
        Optional<Todo> fetchedOpt = todoRepository.findById(savedTodo.getId());

        // Assert
        assertThat(fetchedOpt).isPresent().hasValueSatisfying(
                todo -> assertAll(
                        () -> assertThat(todo.getId()).isEqualTo(savedTodo.getId()),
                        () -> assertThat(todo.getTitle()).isEqualTo(savedTodo.getTitle()),
                        () -> assertThat(todo.getVersion()).isNotNull().isEqualTo(0L),
                        () -> assertThat(todo.getCreatedAt()).isNotNull(),
                        () -> assertThat(todo.getUpdatedAt()).isNotNull(),
                        // 作成時刻と更新時刻がほぼ同じはず
                        () -> assertThat(todo.getCreatedAt()).isCloseTo(todo.getUpdatedAt(), within(1, ChronoUnit.SECONDS)),
                        // 現在時刻とも近いはず（数秒の誤差を許容）
                        () -> assertThat(todo.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS))
                )
        );
    }

    @Test
    @Transactional
    @DisplayName("既存のIDで検索すると正しいTodoEntityが返される")
    void findById_existingTodo_shouldReturnCorrectTodo() {
        // Arrange: テストデータを準備
        Todo targetTodo = new Todo("検索対象タスク");

        // Act
        // EntityManagerを直接使ってテストデータをセットアップ
        entityManager.persist(targetTodo);
        entityManager.flush();
        Long todoId = targetTodo.getId();

        // Assert
        Optional<Todo> fetchedOpt = todoRepository.findById(todoId);
        assertThat(fetchedOpt).isPresent().hasValueSatisfying(
                todo -> assertAll(
                        () -> assertThat(targetTodo.getId()).isEqualTo(todo.getId()),
                        () -> assertThat(targetTodo.getTitle()).isEqualTo(todo.getTitle())
                )
        );
    }

    @Test
    @Transactional
    @DisplayName("存在しないIDで検索するとOptional.emptyが返される")
    void findById_nonExistingTodo_shouldReturnEmpty() {
        // Arrange
        Long nonExistingId = -999L;

        // Act
        Optional<Todo> fetchedOpt = todoRepository.findById(nonExistingId);

        // Assert
        assertThat(fetchedOpt).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("既存のTodoを更新するとフィールド変更され、バージョン外クリメントされ、UpdatedAtが更新される")
    void update_existingTodo_shouldUpdateFieldsAndIncrementVersionAndUpdateTimestamp() {
        // Arrange: 初期データ作成
        var beforeUpdateTitle = "更新テスト";
        var beforeUpdateVersion = 0L;
        Todo beforeUpdateTodo = new Todo(beforeUpdateTitle);
        entityManager.persist(beforeUpdateTodo);
        entityManager.flush();
        // 更新前TodoEntityが登録されている事を確認
        assertAll(
                () -> assertThat(beforeUpdateTodo.getId()).isNotNull(),
                () -> assertThat(beforeUpdateTodo.getTitle()).isNotNull().isEqualTo(beforeUpdateTitle),
                () -> assertThat(beforeUpdateTodo.isDone()).isFalse(),
                () -> assertThat(beforeUpdateTodo.getVersion()).isNotNull().isEqualTo(beforeUpdateVersion),
                () -> assertThat(beforeUpdateTodo.getCreatedAt()).isNotNull().isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS)),
                () -> assertThat(beforeUpdateTodo.getUpdatedAt()).isNotNull().isCloseTo(beforeUpdateTodo.getCreatedAt(), within(1, ChronoUnit.SECONDS))
        );

        // Act
        // updatedAtが確実に変わるように少し待機
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            System.out.println("スレッドが割り込まれました！");
            Thread.currentThread().interrupt();
        }
        Todo fetchedTodo = entityManager.find(Todo.class, beforeUpdateTodo.getId());
        fetchedTodo.complete();
        Todo afterUpdateTodo = entityManager.merge(fetchedTodo);
        entityManager.flush();

        // Assert
        assertThat(afterUpdateTodo).isNotNull().satisfies(
                todo -> assertAll(
                        () -> assertThat(todo.getId()).isEqualTo(beforeUpdateTodo.getId()),
                        () -> assertThat(todo.getTitle()).isEqualTo(beforeUpdateTodo.getTitle()),
                        () -> assertThat(todo.isDone()).isTrue(),
                        () -> assertThat(todo.getVersion()).isEqualTo(beforeUpdateVersion + 1),
                        () -> assertThat(todo.getCreatedAt()).isEqualTo(beforeUpdateTodo.getCreatedAt()),
                        () -> assertThat(ChronoUnit.SECONDS.between(todo.getCreatedAt(), todo.getUpdatedAt())).isGreaterThan(5),
                        () -> assertThat(todo.getUpdatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS))
                )
        );
    }

    @Test
    @Transactional
    @DisplayName("既存のTodoをIdで削除するとDBから削除される")
    void deleteById_existingTodo_shouldRemoveTodo() {
        // Arrange
        Todo targetTodo = new Todo("削除対象タスク");
        entityManager.persist(targetTodo);
        entityManager.flush();
        Long todoId = targetTodo.getId();
        // 削除前に存在することを確認
        assertThat(todoRepository.findById(todoId)).isPresent();

        // Act
        var result = todoRepository.delete(targetTodo);
        assertThat(result).isTrue();
        entityManager.flush();

        // Assert
        // 削除されていることを確認
        assertThat(todoRepository.findById(todoId)).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("複数のTodoが存在する場合、findAllですべて取得できる")
    void findAll_shouldReturnAllTodos() {
        // Arrange
        String title1 = "タスク1";
        String title2 = "タスク2";
        Todo todo1 = new Todo(title1);
        Todo todo2 = new Todo(title2);
        entityManager.persist(todo1);
        entityManager.persist(todo2);
        entityManager.flush();

        // Act
        List<Todo> todos = todoRepository.findAll();

        // Assert
        assertThat(todos).filteredOn(todo -> todo.getId().equals(todo1.getId()) || todo.getId().equals(todo2.getId()))
                .extracting(Todo::getTitle).containsExactlyInAnyOrder(title1, title2);
    }

    @Test
    @Transactional
    @DisplayName("Todoが存在しない場合、findAllはからのリストを返す")
    void findAll_whenNoTodosExist_shouldReturnEmptyList() {
        // Arrange
        entityManager.createQuery("DELETE FROM Todo").executeUpdate();

        // Act
        List<Todo> todos = todoRepository.findAll();

        // Assert
        assertThat(todos).isEmpty();

        // Rollback
        entityManager.getTransaction().rollback();
    }
}
