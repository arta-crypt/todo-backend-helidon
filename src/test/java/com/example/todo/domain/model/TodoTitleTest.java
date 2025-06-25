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

        @Test
        @DisplayName("空白のみの場合は例外が発生する")
        void throwExceptionForWhitespaceOnly() {
            // Given
            String whitespaceOnly = "   ";

            // When & Then
            assertThatThrownBy(() -> new TodoTitle(whitespaceOnly))
                    .as("空白のみの場合は例外が発生する")
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは必須です");
        }

        @Test
        @DisplayName("最大文字数を超える場合は例外が発生する")
        void throwExceptionForTooLongTitle() {
            // Given
            String tooLongTitle = "a".repeat(256);

            // When & Then
            assertThatThrownBy(() -> new TodoTitle(tooLongTitle))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは255文字以内で入力してください");
        }
    }

    @Nested
    @DisplayName("等価性")
    class EqualityTests {

        @Test
        @DisplayName("同じ値を持つインスタンスは等価である")
        void equalityWithSameValue() {
            // Given
            String value = "買い物をする";
            TodoTitle title1 = new TodoTitle(value);
            TodoTitle title2 = new TodoTitle(value);

            // When & Then
            assertThat(title1).isEqualTo(title2);
            assertThat(title1.hashCode()).isEqualTo(title2.hashCode());
        }

        @Test
        @DisplayName("異なる値を持つインスタンスは等価でない")
        void inequalityWithDifferentValue() {
            // Given
            TodoTitle title1 = new TodoTitle("買い物をする");
            TodoTitle title2 = new TodoTitle("洗濯をする");

            // When & Then
            assertThat(title1).isNotEqualTo(title2);
        }

        @Test
        @DisplayName("nullとの飛白では等価ではない")
        void inequalityWithNull() {
            // Given
            TodoTitle title = new TodoTitle("買い物をする");

            // When & Then
            assertThat(title).isNotEqualTo(null);
        }

        @Test
        @DisplayName("異なる型との比較は等価でない")
        void inequalityWithDifferentType() {
            // Given
            TodoTitle title = new TodoTitle("買い物をする");
            String stringValue = "買い物をする";

            // When & Then
            assertThat(title).isNotEqualTo(stringValue);
        }

    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("toStringは値を返す")
        void toStringReturnsValue() {
            // Given
            String titleValue = "買い物をする";
            TodoTitle title = new TodoTitle(titleValue);

            // When
            String result = title.toString();

            // Then
            assertThat(result).isEqualTo(titleValue);
        }
    }
}
