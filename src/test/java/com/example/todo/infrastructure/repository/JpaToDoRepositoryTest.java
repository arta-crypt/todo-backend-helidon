package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.testutil.builder.ToDoBuilder;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JpaToDoRepositoryのテストクラスです。<br>
 * このクラスでは、JPAを使用した「やること」タスクエンティティのリポジトリ操作を検証します。<br>
 * 各テストメソッドは、リポジトリの基本的なCRUD操作や、エンティティの等価性を確認します。<br>
 */
@HelidonTest
@DisplayName("JPAを使用したToDoリポジトリのテスト")
class JpaToDoRepositoryTest {

    /**
     * JpaToDoRepositoryインスタンス。<br>
     * このリポジトリは、JPAを使用して「やること」タスクエンティティのCRUD操作を提供します。
     */
    @Inject
    private JpaToDoRepository repo;

    @Test
    @DisplayName("JPAリポジトリでToDoを保存して取得")
    void testJpaPersist() {
        ToDo todo = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo createdTodo = repo.create(todo);

        Optional<ToDo> found = repo.findById(createdTodo.getId());

        assertAll(
                () -> assertNotNull(createdTodo.getId()),
                () -> assertTrue(found.isPresent()),
                () -> assertEquals("コーヒーを買う", found.get().getTitle())
        );
    }

    @Test
    @DisplayName("存在しないIDで検索した場合にOptional.emptyを返すこと")
    void testFindByIdNotFound() {
        Optional<ToDo> result = repo.findById(99999L);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("ToDoのequalsとhashCodeの動作確認")
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        ToDo todo1 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build().setCreatedAt(now).setUpdatedAt(now);
        ToDo todo2 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build().setCreatedAt(now).setUpdatedAt(now);
        assertAll(
                () -> assertEquals(todo1, todo2),
                () -> assertEquals(todo1.hashCode(), todo2.hashCode())
        );
    }
}