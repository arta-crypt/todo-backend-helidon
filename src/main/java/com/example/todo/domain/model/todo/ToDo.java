package com.example.todo.domain.model.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * ユーザーが管理する「やること」タスクを表すドメインエンティティ。
 * <p>
 * 各タスクは以下の情報を持ちます：
 * <ul>
 *     <li>ID（Long）</li>
 *     <li>タイトル（String）</li>
 *     <li>完了状態（boolean）</li>
 * </ul>
 */
@Entity
public class ToDo {
    @Id
    private Long id;
    private String title;
    private boolean done;

    /**
     * デフォルトコンストラクタ
     */
    public ToDo() {
    }

    /**
     * すべてのフィールドを指定するコンストラクタ
     *
     * @param id    「やること」タスクアイテムのID
     * @param title 「やること」タスクアイテムのタイトル
     * @param done  「やること」タスクアイテムの完了状態
     */
    public ToDo(Long id, String title, boolean done) {
        setId(id);
        setTitle(title);
        setDone(done);
    }

    /**
     * IDを取得します
     *
     * @return 「やること」タスクアイテムのID
     */
    public Long getId() {
        return id;
    }

    /**
     * IDを設定します
     *
     * @param id 設定する「やること」タスクアイテムのID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * タイトルを取得します
     *
     * @return 「やること」タスクアイテムのタイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * タイトルを設定します
     *
     * @param title 設定する「やること」タスクアイテムのタイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 完了状態を取得します
     *
     * @return 「やること」タスクアイテムの完了状態
     */
    public boolean isDone() {
        return done;
    }

    /**
     * 完了状態を設定します
     *
     * @param done 設定する「やること」タスクアイテムの完了状態
     */
    public void setDone(boolean done) {
        this.done = done;
    }
}
