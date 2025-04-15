package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * InMemoryToDoRepositoryu306eu30c6u30b9u30c8
 */
class InMemoryToDoRepositoryTest {
    @DisplayName("「やること」リポジトリの基本操作テスト")
    @Test
    void testInMemoryRepository() {
        // 準備
        Long id = 1L;
        String title = "コーヒーを買う";
        boolean done = false;
        ToDo todo = new ToDo(id, title, done);

        // 実行
        ToDoRepository repo = new InMemoryToDoRepository();
        ToDo saved = repo.save(todo);

        // 検証
        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(title, saved.getTitle()),
                () -> assertEquals(done, saved.isDone()),
                () -> assertTrue(repo.findById(saved.getId()).isPresent()),
                () -> assertTrue(repo.findAll().contains(saved)),
                () -> assertEquals(1, repo.findAll().size())
        );
    }
    
    @DisplayName("deleteメソッドが「やること」タスクを正しく削除すること")
    @Test
    void testDelete() {
        // 準備：リポジトリを生成し、データを追加
        ToDoRepository repo = new InMemoryToDoRepository();
        
        // 2つの「やること」タスクを作成し保存
        ToDo todo1 = new ToDo(null, "牛乳を買う", false);
        ToDo todo2 = new ToDo(null, "実装を完了する", false);
        
        ToDo saved1 = repo.save(todo1);
        ToDo saved2 = repo.save(todo2);
        
        // 初期状態を確認
        assertEquals(2, repo.findAll().size());
        assertTrue(repo.findById(saved1.getId()).isPresent());
        assertTrue(repo.findById(saved2.getId()).isPresent());
        
        // 実行：todo1を削除
        repo.delete(saved1.getId());
        
        // 検証
        assertAll(
                // リストサイズが1になったことを確認
                () -> assertEquals(1, repo.findAll().size()),
                // 削除したアイテムが見つからないことを確認
                () -> assertFalse(repo.findById(saved1.getId()).isPresent()),
                // 削除していないアイテムは存在することを確認
                () -> assertTrue(repo.findById(saved2.getId()).isPresent())
        );
    }
}