package com.example.todo.domain.model;

import com.example.todo.todo.domain.model.TodoId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TodoID のテスト")
public class TodoIdTest {

    @Nested
    @DisplayName("ID作成")
    class IdCreation {

        @DisplayName("新しいIDを作成できる")
        @Test
        void generateNewId() {
            // Given
            Long id = 1L;

            // When
            TodoId todoId = new TodoId(id);

            // Then
            assertThat(todoId).isNotNull().satisfies(
                    todo -> assertThat(todo.getValue()).isEqualTo(id)
            );
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("IDがnullの場合は例外が発生する")
        void throwExceptionForNull() {
            // When & Then
            assertThatThrownBy(() -> new TodoId(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("ID value cannot be null");
        }
    }

    @Nested
    @DisplayName("等価性")
    class Equality {

        @Test
        @DisplayName("同じ値を持つインスタンスは等価である")
        void equalityWithSameValue() {
            // Given
            Long id = 1L;
            TodoId todoId1 = new TodoId(id);
            TodoId todoId2 = new TodoId(id);

            // When & Then
            assertThat(todoId1).isEqualTo(todoId2);
            assertThat(todoId1.hashCode()).isEqualTo(todoId2.hashCode());
        }

        @Test
        @DisplayName("異なる値を持つインスタンスは等価でない")
        void equalityWithDifferentValue() {
            // Given
            Long id1 = 1L;
            Long id2 = 2L;
            TodoId todoId1 = new TodoId(id1);
            TodoId todoId2 = new TodoId(id2);

            // When & Then
            assertThat(todoId1).isNotEqualTo(todoId2);
            assertThat(todoId1.hashCode()).isNotEqualTo(todoId2.hashCode());
        }

        @Test
        @DisplayName("Nullとの比較は等価でない")
        void inequalityWithNull() {
            // Given
            TodoId todoId = new TodoId(1L);

            // When & Then
            assertThat(todoId).isNotEqualTo(null);
        }

        @Test
        @DisplayName("異なる型との比較は等価でない")
        void inequalityWithDifferentType() {
            // Given
            TodoId todoId = new TodoId(1L);
            String stringValue = "1";

            // When & Then
            assertThat(todoId).isNotEqualTo(stringValue);
        }
    }

    @Nested
    @DisplayName("文字列表現")
    class StringRepresentation {

        @Test
        @DisplayName("toString() は値を返す")
        void toStringReturnsValue() {
            // Given
            Long id = 1L;
            TodoId todoId = new TodoId(id);

            // When
            String result = todoId.toString();

            // Then
            assertThat(result).isEqualTo(id.toString());
        }
    }
}
