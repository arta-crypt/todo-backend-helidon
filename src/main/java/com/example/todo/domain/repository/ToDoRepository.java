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
     * 新しいを作成します
     *
     * @param sourceTodo 作成する「やること」タスクエンティティ
     * @return 作成された「やること」タスクエンティティ（IDが設定されたもの）
     * @throws IllegalArgumentException IDが指定されており、それが既に存在する場合
     */
    ToDo create(ToDo sourceTodo);

    /**
     * 既存の「やること」タスクエンティティを更新します
     *
     * @param sourceTodo 更新する「やること」タスクエンティティ
     * @return 更新された「やること」タスクエンティティ（IDが設定されたもの）
     * @throws IllegalArgumentException 指定されたIDが存在しない場合
     */
    ToDo update(ToDo sourceTodo);

    /**
     * 指定されたIDを持つ「やること」タスクエンティティを削除します。
     *
     * @param id 削除対象の「やること」タスクエンティティのID
     */
    void delete(Long id);

    /**
     * 指定されたIDを持つ「やること」タスクエンティティが存在するか確認します
     *
     * @param id 検索対象の「やること」タスクエンティティのID
     * @return 指定されたIDを持つ「やること」タスクエンティティが存在する場合はtrue、存在しない場合はfalse
     */
    boolean existsById(Long id);
}
