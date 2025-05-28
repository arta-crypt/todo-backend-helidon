package com.example.todo.shared.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AuditableEntity {


    /**
     * バージョン番号
     * <p>
     * 楽観ロックや更新管理のために利用
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * TodoTaskの作成日時。
     * <p>
     * レコード生成時に自動設定されます。
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * TodoTaskの最終更新日時。
     * <p>
     * レコード更新時に自動更新されます。
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ====== Setter/Getter ======

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
     * @param version 設定するtodoTaskのバージョン
     * @return 設定されたtodoTask
     */
    protected AuditableEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 作成日時を取得します
     *
     * @return todoTaskの作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 作成日時を設定します
     *
     * @param createdAt 設定するtodoTaskの作成日時
     * @return 設定されたtodoTask
     */
    protected AuditableEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 更新日時を取得します
     *
     * @return todoTaskの更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 更新日時を設定します
     *
     * @param updatedAt 設定するtodoTaskの更新日時
     * @return 設定されたtodoTask
     */
    protected AuditableEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    // ====== ライフサイクルコールバック ======

    /**
     * 新規エンティティが初めて永続化される直前に呼び出されます。
     * 主に createdAt や updatedAt の初期値を設定するために使用します。
     * <p>
     * - 呼び出しタイミング: EntityManager.persist() の直前
     * - 注意: このタイミングではまだ INSERT は発行されていない
     */
    @PrePersist
    public void prePersist() {
        var now = LocalDateTime.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    /**
     * 既存エンティティが更新される直前に呼び出されます。
     * 主に updatedAt を現在時刻に更新するために使用します。
     * <p>
     * - 呼び出しタイミング: EntityManager.merge() の直前
     * - 注意: 必ずしもフィールドが変更されたとは限らないので、無条件に更新される
     */
    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}
