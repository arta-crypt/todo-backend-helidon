package com.example.todo.domain.model.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

/**
 * ユーザーが管理する「やること」タスクを表すドメインエンティティ。
 * <p>
 * 各タスクは以下の情報を持ちます：
 * <ul>
 *     <li>ID（Long）</li>
 *     <li>タイトル（String）</li>
 *     <li>完了状態（boolean）</li>
 *     <li>バージョン（Long）</li>
 *     <li>作成日時（LocalDateTime）</li>
 *     <li>更新日時（LocalDateTime）</li>
 * </ul>
 */
@Entity
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean done;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * デフォルトコンストラクタ
     */
    public ToDo() {
        setVersion(0L);
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 基本的なフィールドを指定するコンストラクタ
     *
     * @param id    「やること」タスクアイテムのID
     * @param title 「やること」タスクアイテムのタイトル
     * @param done  「やること」タスクアイテムの完了状態
     */
    public ToDo(Long id, String title, boolean done) {
        this();
        setId(id);
        setTitle(title);
        setDone(done);
    }

    /**
     * すべてのフィールドを指定するコンストラクタ
     *
     * @param id        「やること」タスクアイテムのID
     * @param title     「やること」タスクアイテムのタイトル
     * @param done      「やること」タスクアイテムの完了状態
     * @param version   「やること」タスクアイテムのバージョン
     * @param createdAt 「やること」タスクアイテムの作成日時
     * @param updatedAt 「やること」タスクアイテムの更新日時
     */
    public ToDo(Long id, String title, boolean done, Long version, LocalDateTime createdAt, LocalDateTime updatedAt) {
        setId(id);
        setTitle(title);
        setDone(done);
        setVersion(version);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }


    /**
     * コピーコンストラクタ
     *
     * @param source コピー元の「やること」タスクアイテム
     * @throws NullPointerException sourceがnullの場合
     */
    public ToDo(ToDo source) {
        if (source == null) {
            throw new NullPointerException("コピー元のオブジェクトがnullです");
        }
        setId(source.getId());
        setTitle(source.getTitle());
        setDone(source.isDone());
        setVersion(source.getVersion());
        setCreatedAt(source.getCreatedAt());
        setUpdatedAt(source.getUpdatedAt());
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
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setId(Long id) {
        this.id = id;
        return this;
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
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
        return this;
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
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setDone(boolean done) {
        this.done = done;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    /**
     * バージョンを取得します
     *
     * @return バージョン
     */
    public Long getVersion() {
        return version;
    }

    /**
     * バージョンを設定します
     *
     * @param version 設定する「やること」タスクアイテムのバージョン
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 作成日時を取得します
     *
     * @return 「やること」タスクアイテムの作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 作成日時を設定します
     *
     * @param createdAt 設定する「やること」タスクアイテムの作成日時
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 更新日時を取得します
     *
     * @return 「やること」タスクアイテムの更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 更新日時を設定します
     *
     * @param updatedAt 設定する「やること」タスクアイテムの更新日時
     * @return 設定された「やること」タスクアイテム
     */
    public ToDo setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
