package com.example.todo.domain.model;

import com.example.todo.todo.domain.model.TodoTitle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TodoTitle のテスト")
public class TodoTitleTest {

    @Nested
    @DisplayName("正常系")
    class ValidCases {

        @Test
        @DisplayName("有効なタイトルでインスタンスを作成できる")
        void createValidTitle() {
            // Given
            String titleValue = "買い物をする";

            // When
            TodoTitle title = new TodoTitle(titleValue);

            // Then
            assertThat(title.getValue()).isEqualTo(titleValue);
        }

        @Test
        @DisplayName("最大文字数でインスタンスを作成できる")
        void createTitleWithMaxLength() {
            // Given
            String titleValue = "a".repeat(255);

            // When
            TodoTitle title = new TodoTitle(titleValue);

            // Then
            assertThat(title.getValue()).isEqualTo(titleValue);
        }

        @Test
        @DisplayName("前後の空白は自動的にトリミングされる")
        void trimWhitespace() {
            // Given
            String titleValue = "   買い物をする   ";
            String expectedValue = "買い物をする";

            // When
            TodoTitle title = new TodoTitle(titleValue);

            // Then
            assertThat(title.getValue()).isEqualTo(expectedValue);
        }
    }

    @Nested
    @DisplayName("異常系")
    class InvalidCases {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null または空文字の場合は例外が発生する")
        void throwExceptionForNullOrEmpty(String invalidValue) {
            // When & Then
            assertThatThrownBy(() -> new TodoTitle(invalidValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは必須です");
        }

        // Todo: 空白のみの場合は例外が発生する

        // Todo: 最大文字数を超える場合は例外が発生する
    }

    // Todo: 等価性のテスト
    // Todo: 同じ値を持つインスタンスは等価である
    // Todo: 異なる値を持つインスタンスは等価でない
    // Todo: Nullとの比較は等価でない
    // Todo: 異なる方との比較は等価でない

    // Todo: toString のテスト
    // Todo: toStringは値を返す
}
