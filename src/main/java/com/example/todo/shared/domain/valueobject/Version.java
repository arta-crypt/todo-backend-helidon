package com.example.todo.shared.domain.valueobject;

import java.util.Objects;


/**
 * 汎用的なVersionを表す値オブジェクトの基底クラス。
 * <p>
 * このクラスを継承して、各エンティティ固有のVersionクラスを作成します。<br>
 * 例：{@code public class TodoVersion extends Version<Long> {...}}
 *
 * @param <T> Versionの実際の値の型（例：{@code Long}, {@code Int}）
 */
public abstract class Version<T extends Number> {

    private final T value;  // Versionの実際の値。不変性を保証するためにfinal

    /**
     * 指定された値でVersionを構成します
     *
     * @param value 　Versionの値。nullであってはならない。0以上でなければならない。
     * @throws NullPointerException     valueがnullの場合
     * @throws IllegalArgumentException valueが0未満の場合
     */
    protected Version(T value) {
        Objects.requireNonNull(value, "Version value cannot be null");

        validateType(value);
        validateNonNegative(value);

        this.value = value;
    }

    /**
     * Versionの実際の値を取得します
     *
     * @return Versionの値
     */
    public T getValue() {
        return value;
    }

    /**
     * Versionの実際の値の型を検証します
     *
     * @param value 検証対象の値
     */
    private void validateType(T value) {
        if (!(value instanceof Integer || value instanceof Long)) {
            throw new IllegalArgumentException("Only Integer and Long are allowed");
        }
    }

    /**
     * Versionの実際の値を検証します
     *
     * @param value 検証対象の値
     */
    private void validateNonNegative(T value) {
        long numericValue = ((Number) value).longValue();
        if (numericValue < 0) {
            throw new IllegalArgumentException("Version value must be greater than or equal to 0");
        }
    }

    /**
     * Versionを1増やす
     *
     * @return 増加後のVersion
     */
    abstract public Version<T> increment();
}

