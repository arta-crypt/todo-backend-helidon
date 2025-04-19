package com.example.todo.testutil.builder;

import com.example.todo.domain.model.todo.ToDo;

import java.time.LocalDateTime;

/**
 * 「やること」タスクエンティティのテストデータビルダー
 */
public class ToDoBuilder {
    /**
     * 「やること」タスクのID
     */
    private Long id;
    /**
     * タイトル（デフォルト：やることサンプル）
     */
    private String title = "やることサンプル";
    /**
     * 完了フラグ（デフォルト：false）
     */
    private boolean done = false;
    /**
     * バージョン（デフォルト：0L）
     */
    private Long version = 0L;
    /**
     * 作成日時（デフォルト：現在日時）
     */
    private LocalDateTime createdAt = LocalDateTime.now();
    /**
     * 更新日時（デフォルト：現在日時）
     */
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * サンプルの「やること」タスクエンティティを生成します。
     * @return デフォルト値が設定された「やること」タスクエンティティ
     */
    public static ToDo sample() {
        return new ToDoBuilder().build();
    }

    /**
     * 完了済みの「やること」タスクエンティティを生成します。
     * @return 完了フラグがtrueの「やること」タスクエンティティ
     */
    public static ToDo completed() {
        return new ToDoBuilder().withDone(true).build();
    }

    /**
     * IDを設定します。
     * @param id 設定するID
     * @return このビルダー自身
     */
    public ToDoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * タイトルを設定します。
     * @param title 設定するタイトル
     * @return このビルダー自身
     */
    public ToDoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 完了フラグを設定します。
     * @param done 完了かどうか
     * @return このビルダー自身
     */
    public ToDoBuilder withDone(boolean done) {
        this.done = done;
        return this;
    }

    /**
     * バージョンを設定します。
     * @param version 設定するバージョン
     * @return このビルダー自身
     */
    public ToDoBuilder withVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 作成日時を設定します。
     * @param createdAt 設定する作成日時
     * @return このビルダー自身
     */
    public ToDoBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 更新日時を設定します。
     * @param updatedAt 設定する更新日時
     * @return このビルダー自身
     */
    public ToDoBuilder withUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * 設定された値で「やること」タスクエンティティを生成します。
     * @return 「やること」タスクエンティティ
     */
    public ToDo build() {
        return new ToDo(id, title, done);
    }
}
