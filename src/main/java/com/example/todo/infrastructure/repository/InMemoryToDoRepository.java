package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;

import java.time.LocalDateTime;
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

        // 必要項目設定
        applyAuditFieldsForCreate(todoToCreate);

        if (todoToCreate.getId() == null) {
            // IDが指定されていない場合は新しいIDを生成
            todoToCreate.setId(generateNewId());
        } else {
            // IDが指定されている場合、既存IDと衝突していないか確認
            Long id = todoToCreate.getId();
            if (store.containsKey(id)) {
                throw new IllegalArgumentException("エンティティ with ID " + id + " already exists.");
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
            throw new IllegalArgumentException("Cannot update エンティティ without ID");
        }

        Long id = sourceTodo.getId();
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("エンティティ with ID " + id + " not found");
        }

        if (!sourceTodo.getVersion().equals(store.get(id).getVersion())) {
            throw new IllegalStateException("Version mismatch");
        }

        ToDo todoToUpdate = new ToDo(sourceTodo);   // ディープコピー
        // 必要項目設定
        applyAuditFieldsForUpdate(todoToUpdate, store.get(id));
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

    /**
     * createメソッドで監査項目（version, createdAt, updatedAt）を初期化します。
     * <ul>
     *   <li>version: 0L</li>
     *   <li>createdAt/updatedAt: 現在日時</li>
     * </ul>
     *
     * @param entity 監査項目を設定するエンティティ
     * @return 設定された「やること」タスクアイテム
     */
    private ToDo applyAuditFieldsForCreate(ToDo entity) {
        LocalDateTime now = LocalDateTime.now();
        return entity.setVersion(0L)
                .setCreatedAt(now)
                .setUpdatedAt(now);
    }

    /**
     * updateメソッドで監査項目（version, createdAt, updatedAt）を更新します。
     * <ul>
     *   <li>version: 既存エンティティのversion+1</li>
     *   <li>createdAt: 既存エンティティのcreatedAtを維持</li>
     *   <li>updatedAt: 現在日時</li>
     * </ul>
     *
     * @param entity   監査項目を設定するエンティティ
     * @param previous 既存のエンティティ@
     * @return 設定された「やること」タスクアイテム
     */
    private ToDo applyAuditFieldsForUpdate(ToDo entity, ToDo previous) {
        return entity.setVersion(previous.getVersion() + 1)
                .setCreatedAt(previous.getCreatedAt())
                .setUpdatedAt(LocalDateTime.now());
    }
}
