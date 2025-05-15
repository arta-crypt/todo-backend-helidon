package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;
import com.example.todo.testutil.builder.ToDoBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * InMemoryToDoRepositoryのテスト
 */
class InMemoryToDoRepositoryTest {

    @DisplayName("「やること」タスクリポジトリのCreateテスト")
    @Test
    void testInMemoryRepositoryCreate() {
        // 準備
        ToDo todo1 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo todo2 = new ToDoBuilder().withTitle("コーヒーを飲む").withDone(false).build();

        // 実行
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo createdTodo1 = repo.create(todo1);
        ToDo createdTodo2 = repo.create(todo2);

        // 検証
        assertAll(
                // 登録前のレコードのIDがNULLであることの確認
                () -> assertNull(todo1.getId()),
                () -> assertNull(todo2.getId()),
                // 登録後のレコードのIDがNULLでないことの確認
                () -> assertNotNull(createdTodo1.getId()),
                () -> assertNotNull(createdTodo2.getId()),
                // 登録前後でレコードの内容が一致することの確認
                () -> assertEquals(todo1.getTitle(), createdTodo1.getTitle()),
                () -> assertEquals(todo2.getTitle(), createdTodo2.getTitle()),
                // 登録レコード数が一致することの確認
                () -> assertEquals(2, repo.findAll().size())
        );
    }

    @DisplayName("「やること」タスクリポジトリの参照テスト")
    @Test
    void testInMemoryRepositorySelect() {
        // 準備
        ToDo todo1 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo todo2 = new ToDoBuilder().withTitle("コーヒーを飲む").withDone(false).build();
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo createdTodo1 = repo.create(todo1);
        ToDo createdTodo2 = repo.create(todo2);

        // 実行&検証
        assertAll(
                // IDで検索できることの確認
                () -> assertEquals(createdTodo1, repo.findById(createdTodo1.getId()).get()),
                () -> assertEquals(createdTodo2, repo.findById(createdTodo2.getId()).get()),
                // すべてのレコードを取得できることの確認
                () -> assertEquals(2, repo.findAll().size())
        );
    }

    @DisplayName("「やること」タスクリポジトリのUpdateテスト")
    @Test
    void testInMemoryRepositoryUpdate() {
        // 準備
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo createdToDo1 = repo.create(new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build());
        ToDo createdToDo2 = repo.create(new ToDoBuilder().withTitle("コーヒーを飲む").withDone(false).build());

        // 実行
        ToDo updatedTodo2 = repo.update(new ToDo(createdToDo2).setDone(true));

        // 検証
        assertAll(
                // 更新前と更新後でIDに変化ないことの確認
                () -> assertEquals(createdToDo1.getId(), repo.findById(createdToDo1.getId()).get().getId()),
                () -> assertEquals(createdToDo2.getId(), updatedTodo2.getId()),
                // 更新していないDoneは変化ないことを確認
                () -> assertEquals(createdToDo1.isDone(), repo.findById(createdToDo1.getId()).get().isDone()),
                // 更新前と更新後でDoneが変化した事の確認
                () -> assertNotEquals(createdToDo2.isDone(), updatedTodo2.isDone()),
                // Doneの実際の値を確認
                () -> assertFalse(repo.findById(createdToDo1.getId()).get().isDone()),
                () -> assertTrue(repo.findById(createdToDo2.getId()).get().isDone())
        );
    }

    @DisplayName("deleteメソッドが「やること」タスクを正しく削除すること")
    @Test
    void testDelete() {
        // 準備
        ToDo todo1 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo todo2 = new ToDoBuilder().withTitle("コーヒーを飲む").withDone(false).build();
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo createdTodo1 = repo.create(todo1);
        ToDo createdTodo2 = repo.create(todo2);

        // 初期状態を確認
        assertEquals(2, repo.findAll().size());
        assertTrue(repo.findById(createdTodo1.getId()).isPresent());
        assertTrue(repo.findById(createdTodo2.getId()).isPresent());

        // 実行：todo1を削除
        repo.delete(createdTodo1.getId());

        // 検証
        assertAll(
                // リストサイズが1になったことを確認
                () -> assertEquals(1, repo.findAll().size()),
                // 削除したアイテムが見つからないことを確認
                () -> assertFalse(repo.findById(createdTodo1.getId()).isPresent()),
                // 削除していないアイテムは存在することを確認
                () -> assertTrue(repo.findById(createdTodo2.getId()).isPresent())
        );
    }

    @DisplayName("「やること」タスクリポジトリのexistsByIdメソッドテスト")
    @Test
    void testExistsById() {
        // 準備
        ToDo todo1 = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo todo2 = new ToDoBuilder().withTitle("コーヒーを飲む").withDone(false).build();
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo createdTodo1 = repo.create(todo1);
        ToDo createdTodo2 = repo.create(todo2);

        // 実行
        boolean result1 = repo.existsById(createdTodo1.getId());
        boolean result2 = repo.existsById(createdTodo2.getId());
        boolean result3 = repo.existsById(createdTodo2.getId() + 1);

        // 検証
        assertAll(
                // レコードの存在確認ができることの確認
                () -> assertTrue(result1),
                () -> assertTrue(result2),
                // 存在しないレコードが判断できることの確認
                () -> assertFalse(result3)
        );
    }

    @DisplayName("監査項目（version, createdAt, updatedAt）が自動設定されること")
    @Test
    void testAuditFieldsOnCreateAndUpdate() {
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo entity = new ToDoBuilder().withId(null).withTitle("監査テスト").withDone(false).withVersion(null).withCreatedAt(null).withUpdatedAt(null).build();
        ToDo created = repo.create(entity);
        assertAll(
                () -> assertEquals(0L, created.getVersion()),
                () -> assertNotNull(created.getCreatedAt()),
                () -> assertNotNull(created.getUpdatedAt()),
                () -> assertEquals(created.getCreatedAt(), created.getUpdatedAt())
        );
        // 更新
        ToDo updated = repo.update(new ToDo(created).setDone(true));
        assertAll(
                () -> assertEquals(1L, updated.getVersion()),
                () -> assertEquals(created.getCreatedAt(), updated.getCreatedAt()),
                () -> assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()))
        );
    }

    @DisplayName("version不一致時に例外が発生すること")
    @Test
    void testVersionMismatchThrowsException() {
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo created = repo.create(new ToDoBuilder().withTitle("versionテスト").build());
        // versionを変更して不正な更新
        ToDo invalid = new ToDo(created).setVersion(created.getVersion() + 10);
        Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> repo.update(invalid));
        assertTrue(ex.getMessage().contains("Version mismatch"));
    }
}