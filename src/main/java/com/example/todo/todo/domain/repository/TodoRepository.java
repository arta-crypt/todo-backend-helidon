package com.example.todo.todo.domain.repository;

import com.example.todo.todo.infrastructure.persistence.entity.Todo;

import java.util.List;
import java.util.Optional;

/**
 * TodoEntityのリポジトリインターフェース
 * <p>
 * DDD（ドメイン駆動設計）パターンに従い、ドメイン層でインターフェースを定義<br>
 * 実装はinfrastructure層で行う
 */
public interface TodoRepository {

    /**
     * 新しいTodoEntityを保存する
     *
     * @param todoEntity 保存するTodoEntity
     * @return 保存されたTodoEntity（IDが設定される）
     */
    Todo save(Todo todoEntity);

    /**
     * IDでTodoEntityを検索する
     *
     * @param id 検索するTodoEntityのID
     * @return 見つかったTodoEntity、存在しない場合は{@code Optional.empty()}
     */
    Optional<Todo> findById(Long id);

    /**
     * すべてのTodoEntityを取得する
     *
     * @return 全TodoEntityのリスト
     */
    List<Todo> findAll();

    /**
     * 完了状態でTodoEntityを検索する
     *
     * @param completed 完了フラグ（{@code true}：完了済、{@code false}：未完了）
     * @return
     */
    List<Todo> findByCompleted(boolean completed);

    /**
     * タイトルでTodoEntityを部分検索する（大文字・小文字区別なし）
     *
     * @param title 検索するタイトル（部分一致）
     * @return マッチしたTodoEntityのタイトル
     */
    List<Todo> findByTitleContainingIgnoreCase(String title);

    /**
     * 既存のTodoEntityを更新する
     *
     * @param todoEntity 更新するTodoEntity
     * @return 更新されたTodoEntity
     * @throws IllegalArgumentException {@code id}が設定されていない場合
     */
    Todo update(Todo todoEntity);

    /**
     * IDでTodoEntityを削除する
     *
     * @param id 削除するTodoEntityのID
     * @return 削除が成功した場合 {@code true}、対象が存在しない場合{@code false}
     */
    boolean deleteById(Long id);

    /**
     * すべてのTodoEntityを削除する（テスト用）
     *
     * @return 削除されたTodoEntityの件数
     */
    long deleteAll();

    /**
     * TodoEntityの総数を取得する
     *
     * @return 登録されているTodoEntityの総数
     */
    long count();

    /**
     * 完了済みのTodoEntityの数を取得する
     *
     * @return 完了済みTodoEntityの件数
     */
    long countByCompleted();

    /**
     * 指定したIDのTodoEntityが存在するかチェック
     *
     * @param id チェックするTodoEntityのID
     * @return 存在する場合 {@code true}
     */
    boolean existById(Long id);
}
