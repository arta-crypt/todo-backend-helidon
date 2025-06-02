package com.example.todo.todo.infrastructure.persistence.repository;

import com.example.todo.todo.domain.repository.TodoRepository;
import com.example.todo.todo.infrastructure.persistence.entity.Todo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * TodoRepositoryの実装クラス
 * <p>
 * JPA/Hibernateを使用してデータベースサクセスを実装<br>
 * CDI @ApplicationScoped で単一インスタンスとして管理
 */
@ApplicationScoped
@Transactional
public class TodoRepositoryImpl implements TodoRepository {
    private static final Logger logger = LogManager.getLogger(TodoRepositoryImpl.class);

    @PersistenceContext(unitName = "todoPU")
    private EntityManager entityManager;

    @Override
    public Todo save(Todo todoEntity) {
        logger.debug("Todo保存開始: {}", todoEntity);

        try {
            if (todoEntity.getId() == null) {
                // 新規作成
                entityManager.persist(todoEntity);
                logger.debug("新規Todo作成完了: ID={}", todoEntity.getId());
            } else {
                // このメソッドでの更新は許可しない
                throw new UnsupportedOperationException("更新は許可されていません");
            }
            entityManager.flush();  // すぐにDBに反映
            return todoEntity;
        } catch (Exception e) {
            logger.error("Todo保存エラー: {}", todoEntity, e);
            throw new RuntimeException("Todoの保存に失敗しました", e);
        }
    }

    @Override
    public Optional<Todo> findById(Long id) {
        logger.debug("Todo検索開始: ID={}", id);

        if (id == null) {
            logger.warn("IDがnullのためTodo検索をスキップ");
            return Optional.empty();
        }

        try {
            Todo todo = entityManager.find(Todo.class, id);
            if (todo != null) {
                logger.debug("Todo検索成功: ID={}", id);
                return Optional.of(todo);
            } else {
                logger.debug("Todo検索結果なし: ID={}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Todo検索エラー: ID={}", id, e);
            throw new RuntimeException("Todoの検索に失敗しました", e);
        }
    }

    @Override
    public List<Todo> findAll() {
        logger.debug("全Todo検索開始");

        try {
            TypedQuery<Todo> query = entityManager.createQuery(
                    "SELECT t FROM Todo t ORDER BY t.createdAt DESC", Todo.class);

            List<Todo> todos = query.getResultList();
            logger.debug("全Todo検索成功: {}件", todos.size());
            return todos;
        } catch (Exception e) {
            logger.error("全Todo検索エラー", e);
            throw new RuntimeException("全Todoの検索に失敗しました", e);
        }
    }

    @Override
    public List<Todo> findByCompleted(boolean isDone) {
        logger.debug("完了状態別Todo検索開始: done={}", isDone);

        try {
            TypedQuery<Todo> query = entityManager.createQuery(
                    "SELECT t FROM Todo t WHERE t.done = :isDone ORDER BY t.createdAt DESC", Todo.class);
            query.setParameter("isDone", isDone);

            List<Todo> todos = query.getResultList();
            logger.debug("完了状態別Todo検索完了: {}件", todos.size());
            return todos;
        } catch (Exception e) {
            logger.error("完了状態別Todo検索エラー: isDone={}", isDone, e);
            throw new RuntimeException("完了状態別Todoの検索に失敗しました", e);
        }
    }

    @Override
    public List<Todo> findByTitleContainingIgnoreCase(String title) {
        logger.debug("タイトル部分検索開始: title={}", title);

        if (title == null || title.trim().isEmpty()) {
            logger.warn("タイトルが空のため全件検索");
            return findAll();
        }

        try {
            String serachPattern = "%" + title.toLowerCase() + "%";
            TypedQuery<Todo> query = entityManager.createQuery(
                    "SELECT t FROM Todo t WHERE LOWER(t.title) LIKE :title ORDER BY t.createdAt DESC", Todo.class);
            query.setParameter("title", serachPattern);

            List<Todo> todos = query.getResultList();
            logger.debug("タイトル部分検索完了: {}件 (title={})", todos.size(), title);
            return todos;
        } catch (Exception e) {
            logger.error("タイトル部分検索エラー: title={}", title, e);
            throw new RuntimeException("タイトル部分検索に失敗しました", e);
        }
    }

    @Override
    public Todo update(Todo sourceTodo) {
        logger.debug("Todo更新開始: {}", sourceTodo);

        if (sourceTodo.getId() == null) {
            throw new IllegalArgumentException("更新対象のTodoにIDが設定されていません");
        }

        try {
            // エンティティが存在するかチェック
            if (!existById(sourceTodo.getId())) {
                throw new RuntimeException("更新対象のTodoが存在しません: ID=" + sourceTodo.getId());
            }

            Todo updated = entityManager.merge(sourceTodo);
            entityManager.flush();

            logger.debug("Todo更新完了: {}", updated);
            return updated;
        } catch (Exception e) {
            logger.error("Todo更新エラー: {}", sourceTodo, e);
            throw new RuntimeException("Todoの更新に失敗しました", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        logger.debug("Todo削除開始: ID={}", id);

        if (id == null) {
            logger.warn("IDがnullのためTodo削除をスキップ");
            return false;
        }

        try {
            Optional<Todo> todo = findById(id);
            if (todo.isPresent()) {
                entityManager.remove(todo.get());
                entityManager.flush();
                logger.debug("Todo削除完了: ID={}", id);
                return true;
            } else {
                logger.debug("削除対象のTodoが存在しません: ID={}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Todo削除エラー: ID={}", id, e);
            throw new RuntimeException("Todoの削除に失敗しました", e);
        }
    }

    @Override
    public boolean delete(Todo todoEntity) {
        if (todoEntity == null || todoEntity.getId() == null) {
            logger.warn("削除対象のTodoまたはIDがnullです");
            return false;
        }
        return deleteById(todoEntity.getId());
    }

    @Override
    public long deleteAll() {
        logger.debug("全Todo削除開始");

        try {
            int deletedCount = entityManager.createQuery("DELETE FROM Todo").executeUpdate();
            entityManager.flush();
            logger.debug("全Todo削除完了: {}件", deletedCount);
            return deletedCount;
        } catch (Exception e) {
            logger.error("全Todo削除エラー", e);
            throw new RuntimeException("全Todoの削除に失敗しました", e);
        }
    }

    @Override
    public long count() {
        logger.debug("Todo件数取得開始");

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Todo t", Long.class);
            Long count = query.getSingleResult();
            logger.debug("Todo件数取得完了: {}件", count);
            return count;
        } catch (Exception e) {
            logger.error("Todo件数取得エラー", e);
            throw new RuntimeException("Todo件数の取得に失敗しました", e);
        }
    }

    @Override
    public long countByCompleted() {
        logger.debug("完了済みのTodo件数取得開始");

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Todo t WHERE t.done = true", Long.class);
            Long count = query.getSingleResult();
            logger.debug("完了済みのTodo件数取得完了: {}件", count);
            return count;
        } catch (Exception e) {
            logger.error("完了済みのTodo件数取得エラー", e);
            throw new RuntimeException("完了済みのTodo件数の取得に失敗しました", e);
        }
    }

    @Override
    public long countByPending() {
        logger.debug("未完了Todo件数取得開始");

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Todo t WHERE t.done = false", Long.class);
            Long count = query.getSingleResult();
            logger.debug("未完了Todo件数取得完了: {}件", count);
            return count;
        } catch (Exception e) {
            logger.error("未完了Todo件数取得エラー", e);
            throw new RuntimeException("未完了Todo件数の取得に失敗しました", e);
        }
    }

    @Override
    public boolean existById(Long id) {
        if (id == null) {
            return false;
        }

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Todo t WHERE t.id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Todo存在チェックエラー: ID={}", id, e);
            throw new RuntimeException("Todoの存在チェックに失敗しました", e);
        }
    }
}
