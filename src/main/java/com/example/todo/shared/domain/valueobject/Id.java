package com.example.todo.shared.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 汎用的なIDを表す値オブジェクトの基底クラス。
 * <p>
 * このクラスを継承して、各エンティティ固有のIDクラスを作成します。<br>
 * 例：{@code public class TodoID extends Id<UUID> {...}}
 *
 * @param <T> IDの実際の値の型（例：{@code UUID}, {@code Long}, {@code String}）
 */
public abstract class Id<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;    // SerializableのためのバージョンUID
    private final T value;                              // IDの実際の値。不変性を保証するためにfinal

    /**
     * 指定された値でIDを構成します
     *
     * @param value 　IDの値。nullであってはならない。
     * @throws NullPointerException valueがnullの場合
     */
    protected Id(T value) {
        // IDの値がnullでないことを保証
        Objects.requireNonNull(value, "ID value cannot be null");
        this.value = value;
    }

    /**
     * IDの実際の値を取得します
     *
     * @return IDの値
     */
    public T getValue() {
        return value;
    }

    /**
     * このIDが指定されたオブジェクトと等しいかどうかを比較します。
     * <p>
     * オブジェクトが{@code null}でなく、同じクラスであり、かつ内部の値が等しい場合に{@code true}を返します。
     *
     * @param o 比較対象のオブジェクト
     * @return このIDが指定されたオブジェクトと等しい場合は {@code true}、そうでない場合は{@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 同一インスタンスの場合はtrue
        if (o instanceof Id<?> that) {
            return value.equals(that.value);    // 内部の値で比較
        }
        return false;
    }

    /**
     * このIDのハッシュコードを返します。
     * <p>
     * 内部の値のハッシュコードに基づきます。
     *
     * @return このIDのハッシュコード値
     */
    @Override
    public int hashCode() {
        return Objects.hash(value); // 内部の値でハッシュコードを生成
    }

    /**
     * このIDの文字列表現を返します。
     * <p>
     * 通常、内部の値の文字列表現を返します。
     *
     * @return このIdの文字列表現
     */
    @Override
    public String toString() {
        return value.toString();    // 内部の値の文字列表現
    }
}
