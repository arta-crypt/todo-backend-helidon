package com.example.todo.infrastructure.persistence.repository;

import com.example.todo.todo.domain.repository.TodoRepository;
import com.example.todo.todo.infrastructure.persistence.entity.Todo;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
        // DBによって自動設定される値を確認するため、一度flushして永続化コンテキストをクリアし、再取得する
        entityManager.flush();  // 変更をDBに同期
        entityManager.clear();  // 永続化コンテキストをクリアし、キャッシュされていない状態にする
        Optional<Todo> fetchedTodo = todoRepository.findById(savedTodo.getId());

        // Assert
        assertThat(fetchedTodo).isPresent().hasValueSatisfying(
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
}
