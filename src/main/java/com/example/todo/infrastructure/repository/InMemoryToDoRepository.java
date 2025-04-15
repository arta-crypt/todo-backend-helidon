package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryToDoRepository implements ToDoRepository {
    private final Map<Long, ToDo> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Optional<ToDo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ToDo> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public ToDo save(ToDo sourceTodo) {
        if (sourceTodo.getId() == null) {
            // incrementAndGetを使用して、インクリメント後の値をIDとして設定
            sourceTodo.setId(idGenerator.incrementAndGet());
        } else {
            // 既存のIDが存在するか確認
            // もし存在しなければ、IDジェネレーターを更新して将来の重複を防ぐ
            if (!store.containsKey(sourceTodo.getId())) {
                // 既存IDがidGeneratorの現在値以上なら、idGeneratorを更新
                idGenerator.updateAndGet(currentId ->
                        Math.max(currentId, sourceTodo.getId() + 1));
            }
            // IDを設定
            sourceTodo.setId(idGenerator.get());
        }
        // ディープコピーを作成して変更不可にする
        ToDo savedTodo = new ToDo(sourceTodo);
        store.put(savedTodo.getId(), savedTodo);
        return savedTodo;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
