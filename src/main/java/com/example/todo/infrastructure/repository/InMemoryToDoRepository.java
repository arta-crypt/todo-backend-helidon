package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryToDoRepository implements ToDoRepository {
    /**
     * 「やること」タスクオブジェクトを格納するインメモリストレージ。
     * キーは「やること」タスクのID、値は「やること」タスクオブジェクト。
     */
    private final Map<Long, ToDo> store = new HashMap<>();

    /**
     * 「やること」タスクオブジェクトのID生成に使用する原子的カウンター。
     * スレッドセーフな方法で一意のIDを生成するために使用します。
     */
    private final AtomicLong idGenerator = new AtomicLong(0L);

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ToDo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ToDo> findAll() {
        return List.copyOf(store.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToDo create(ToDo sourceTodo) {
        ToDo todoToCreate = new ToDo(sourceTodo);   // ディープコピー

        if (todoToCreate.getId() == null) {
            // IDが指定されていない場合は新しいIDを生成
            todoToCreate.setId(generateNewId());
        } else {
            // IDが指定されている場合、既存IDと衝突していないか確認
            Long id = todoToCreate.getId();
            if (store.containsKey(id)) {
                throw new IllegalArgumentException("ToDo with ID " + id + " already exists.");
            }

            // 将来のID生成で衝突しないようにIDジェネレーターを更新
            updateIdGenerator(id);
        }

        // 保存
        store.put(todoToCreate.getId(), todoToCreate);
        return todoToCreate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToDo update(ToDo sourceTodo) {
        if (sourceTodo.getId() == null) {
            throw new IllegalArgumentException("Cannot update ToDo without ID");
        }

        Long id = sourceTodo.getId();
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("ToDo with ID " + id + " not found");
        }

        ToDo todoToUpdate = new ToDo(sourceTodo);   // ディープコピー
        store.put(id, todoToUpdate);
        return todoToUpdate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return id != null && store.containsKey(id);
    }

    /**
     * 新しい一意のIdを生成します
     *
     * @return 生成された一意のID
     */
    private Long generateNewId() {
        return idGenerator.incrementAndGet();
    }

    /**
     * IDジェネレーターの値を更新して、将来のID生成で衝突しないようにします
     *
     * @param id 考慮すべきID
     */
    private void updateIdGenerator(Long id) {
        idGenerator.updateAndGet(current -> Math.max(current, id + 1));
    }
}
