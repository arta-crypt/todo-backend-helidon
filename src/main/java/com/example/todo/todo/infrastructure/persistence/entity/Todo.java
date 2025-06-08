package com.example.todo.todo.infrastructure.persistence.entity;

import com.example.todo.shared.domain.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ユーザーが管理するTodoTaskを表すドメインエンティティ
 * <p>
 * 　各タスクは以下の情報を持つ：
 * <ul>
 *     <li>ID(Long</li>
 *     <li>タイトル(String)</li>
 *     <li>完了状態(boolean)</li>
 *     <li>バージョン番号(Long)</li>
 *     <li>作成日時(LocalDateTime)</li>
 *     <li>更新日時(LocalDatetime)</li>
 * </ul>
 */
@Entity
@Table(name = "todos")
public class Todo extends AuditableEntity {

    /**
     * TodoTaskの一意な識別子
     * <p>
     * データベース上で自動採番される
     */
    @Id
    @SequenceGenerator(
            name = "TODO_ID_SEQ_GENERATOR", // シーケンスジェネレータの名前
            sequenceName = "TODO_ID_SEQ",   // Oracle DB上のシーケンス名
            allocationSize = 1              // シーケンスから一度に取得するIDの数
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TODO_ID_SEQ_GENERATOR" // @SequenceGeneratorで定義した名前
    )
    @Column(name = "id", nullable = false, updatable = false)   // カラム名を指定
    private Long id;

    /**
     * TodoTaskのタイトル
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * TodoTaskの完了状態
     */
    @Column(name = "done", nullable = false)
    private boolean done;

    /**
     * デフォルトコンストラクタ
     *
     * @param title TodoTaskのタイトル
     */
    public Todo(String title) {
        LocalDateTime now = LocalDateTime.now();
        setTitle(title);
        setVersion(0L);
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    /**
     * すべてのフィールドを指定するコンストラクタ
     *
     * @param id        TodoTaskのID
     * @param title     TodoTaskのタイトル
     * @param done      TodoTaskの完了状態
     * @param version   TodoTaskのバージョン
     * @param createdAt TodoTaskの作成日時
     * @param updatedAt TodoTaskの更新日時
     */
    public Todo(Long id, String title, boolean done, Long version, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
     * @param source コピー元のTodoTask
     * @throws NullPointerException sourceがnullの場合
     */
    public Todo(Todo source) {
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

    // ====== Setter/Getter ======

    /**
     * IDを取得します
     *
     * @return todoTaskのID
     */
    public Long getId() {
        return id;
    }

    /**
     * IDを設定します
     *
     * @param id 設定するtodoTaskのID
     * @return 設定されたtodoTask
     */
    private Todo setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * タイトルを取得します
     *
     * @return todoTaskのタイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * タイトルを設定します
     *
     * @param title 設定するtodoTaskのタイトル
     * @return 設定されたtodoTask
     */
    public Todo setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 完了状態を取得します
     *
     * @return todoTaskの完了状態
     */
    public boolean isDone() {
        return done;
    }

    /**
     * 完了状態を設定します
     *
     * @param done 設定するtodoTaskの完了状態
     * @return 設定されたtodoTask
     */
    private Todo setDone(boolean done) {
        this.done = done;
        return this;
    }

    /**
     * 未完了状態に設定します
     *
     * @return 設定されたtodoTask
     */
    public Todo reopen() {
        return setDone(false);
    }

    /**
     * 完了状態に設定します
     *
     * @return 設定されたtodoTask
     */
    public Todo complete() {
        return setDone(true);
    }

    // ====== Equals/HashCode ======

    /**
     * このエンティティが指定されたオブジェクトと等しいかどうかを比較します。
     * <ul>
     *  <li>オブジェクトが同一インスタンスであれば true。</li>
     *  <li>オブジェクトが null または異なるクラスであれば false。</li>
     *  <li>両方のエンティティの id が null であれば false (永続化されていない異なるインスタンスは等しくないとみなす)。</li>
     *  <li>両方のエンティティの id が null でなく、かつ等しければ true。</li>
     *  <li>上記以外の場合は false。</li>
     * </ul>
     *
     * @param o 比較対象のオブジェクト
     * @return このエンティティが指定されたオブジェクトと等しい場合は true、そうでない場合は false
     */
    @Override
    public boolean equals(Object o) {
        // 同一インスタンスのチェック
        if (this == o) return true;
        // nullチェックとクラスタイプのチェック
        if (!(o instanceof Todo that)) return false;
        // id が null の場合 (永続化されていないエンティティ)
        // 両方のidがnullでも、異なるインスタンスであれば等しくありません。
        if (this.id == null || that.id == null) return false;
        // IDで比較
        return Objects.equals(id, that.id);
    }

    /**
     * このエンティティのハッシュコードを返します。
     * {@link #equals(Object)} メソッドと一貫性を持たせるため、
     * id が null でない場合は id のハッシュコードを、
     * id が null の場合は定数 (ここでは Objects.hash(null) で 0) を返します。
     *
     * @return このエンティティのハッシュコード
     */
    @Override
    public int hashCode() {
        // id が null の場合、Objects.hash(null) は 0 を返します。
        // これにより、永続化されていない複数のインスタンスが同じハッシュコードを持つ可能性がありますが、
        // equals メソッドがそれらを区別するため、HashSetなどのコレクションで正しく動作します。
        return Objects.hash(id);
    }

    // ====== toString ======

    /**
     * TodoTaskの内容を文字列形式で返します。
     *
     * @return TodoTaskの内容を含む文字列
     */
    @Override
    public String toString() {
        return String.format(
                "Todo{id=%d, title=%s, done=%B, version=%d, created_at=%s, updated_at=%s}",
                getId(), getTitle(), isDone(), getVersion(),
                getCreatedAt().toString(), getUpdatedAt().toString()
        );
    }
}
