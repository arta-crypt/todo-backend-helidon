package com.example.todo.todo.domain.model;


/**
 * TodoModelのステータスを表す列挙型
 * <p>
 * ビジネルスール:
 * <ul>
 *     <li>TODO_:未完了状態</li>
 *     <li>DONE :完了状態</li>
 * </ul>
 */
public enum TodoDone {

    /**
     * 未完了状態を表す列挙子
     */
    TODO("未完了", false),
    /**
     * 完了状態を表す列挙子
     */
    DONE("完了", true);

    private final String displayName;
    private final boolean completed;

    /**
     * コンストラクタ。
     *
     * @param displayName 表示名
     * @param completed   完了状態（true: 完了, false: 未完了）
     */
    TodoDone(String displayName, boolean completed) {
        this.displayName = displayName;
        this.completed = completed;
    }

    /**
     * 表示名を取得する
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 完了状態かどうかを取得する
     *
     * @return 完了状態の場合 {@code true}, 未完了状態の場合 {@code false}
     */
    public boolean isDone() {
        return completed;
    }

    /**
     * 完了状態かどうかを取得する
     *
     * @return 完了状態の場合 {@code true}, 未完了状態の場合 {@code false}
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * このオブジェクトの文字列表現を返す。
     *
     * @return 表示名
     */
    @Override
    public String toString() {
        return displayName;
    }
}
