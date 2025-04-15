package com.example.todo.domain.repository;

import com.example.todo.domain.model.todo.ToDo;

import java.util.List;
import java.util.Optional;

/**
 * 「やること」タスクリポジトリインターフェース
 * <p>
 * 「やること」タスクエンティティの永続化と取得を担当するリポジトリの契約を定義します。
 * このインターフェースは「やること」タスクエンティティの基本的なCRUD操作を提供します。
 * </p>
 */
public interface ToDoRepository {
    /**
     * 指定されたIDを持つ「やること」タスクエンティティを検索します。
     *
     * @param id 検索対象の「やること」タスクエンティティのID
     * @return 指定されたIDを持つ「やること」タスクエンティティを含むOptional。エンティティが存在しない場合は空のOptional
     */
    Optional<ToDo> findById(Long id);

    /**
     * すべての「やること」タスクエンティティを取得します。
     *
     * @return すべての「やること」タスクエンティティのリスト。エンティティが存在しない場合は空のリスト
     */
    List<ToDo> findAll();

    /**
     * 「やること」タスクエンティティを保存します。
     * <p>
     * エンティティが既に存在する場合は更新し、存在しない場合は新規作成します。
     * </p>
     *
     * @param sourceTodo 保存操作のソースとなる「やること」タスクエンティティ
     * @return 保存された「やること」タスクエンティティ（IDが割り当てられた状態）
     */
    ToDo save(ToDo sourceTodo);

    /**
     * 指定されたIDを持つ「やること」タスクエンティティを削除します。
     *
     * @param id 削除対象の「やること」タスクエンティティのID
     */
    void delete(Long id);
}
