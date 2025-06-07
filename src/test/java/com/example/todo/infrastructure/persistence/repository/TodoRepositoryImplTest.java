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
import java.util.Optional;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
//        entityManager.clear();
    }

    @Test
    @Transactional  // 各テストメソッドをトランザクション内で実行し、テスト後にロールバック
    @DisplayName("新しいTodoを保存するとID、バージョン、タイムスタンプがDBで自動設定される")
    void save_newTodo_shouldPersistAndSetGeneratedValues() {
        // Arrange
        Todo newTodo = new Todo("新しいDBテストタスク");

        //Act
        Todo savedTodo = todoRepository.save(newTodo);
        assertAll(
                () -> assertThat(savedTodo).isNotNull(),
                () -> assertThat(savedTodo.getId()).isNotNull()
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
}
