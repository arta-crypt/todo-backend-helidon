package com.example.todo.todo.domain.model;

import java.util.Objects;

/**
 * TodoModelのタイトルを表す値オブジェクト
 * <p>
 * ビジネスルール：
 * <ul>
 * <li>タイトルは必須（null、空文字、空白のみは不可）</li>
 * <li>最大255文字</li>
 * <li>前後の空白は自動的にトリミング</li>
 * </ul>
 */
public class TodoTitle {

    /**
     * 最大文字数
     */
    private static final int MAX_LENGTH = 255;

    /**
     * タイトル値
     */
    private final String value;

    /**
     * コンストラクタ
     *
     * @param value タイトル値
     * @throws IllegalArgumentException タイトルが無効な場合
     */
    public TodoTitle(String value) {
        this.value = validateAndNormalize(value);
    }

    /**
     * タイトル値を取得する
     *
     * @return タイトル値
     */
    public String getValue() {
        return value;
    }

    /**
     * 値の検証と正規化を行う
     *
     * @param value 検証対象の値
     * @return 正規化された値
     * @throws IllegalArgumentException 値が無効な場合
     */
    private String validateAndNormalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }

        String normalizedValue = value.trim();

        if (normalizedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("タイトルは%d文字以内で入力してください", MAX_LENGTH)
            );
        }

        return normalizedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 同一インスタンスの場合はtrue
        if (o instanceof TodoTitle that) {
            return value.equals(that.value);    // 内部の値で比較
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
