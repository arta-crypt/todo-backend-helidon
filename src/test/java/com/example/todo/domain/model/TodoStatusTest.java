package com.example.todo.domain.model;

import com.example.todo.todo.domain.model.TodoStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("TodoStatus のテスト")
public class TodoStatusTest {

    @Nested
    @DisplayName("ステータス作成")
    class DoneCreation {

        @Test
        @DisplayName("TODOステータスを作成できる")
        void createTodoStatus() {
            // Given
            TodoStatus todoStatus = TodoStatus.TODO;

            // Then
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(todoStatus.getDisplayName()).isEqualTo("未完了");
                        softAssertions.assertThat(todoStatus.isDone()).isFalse();
                    }
            );
        }

        @Test
        @DisplayName("DONEステータスを作成できる")
        void createDoneStatus() {
            // Given
            TodoStatus todoStatus = TodoStatus.DONE;

            // Then
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(todoStatus.getDisplayName()).isEqualTo("完了");
                        softAssertions.assertThat(todoStatus.isDone()).isTrue();
                    }
            );
        }
    }

    @Nested
    @DisplayName("等価性とハッシュコード")
    class EqualityAndHashCode {

        @Test
        @DisplayName("同じステータスは等価である")
        void sameStatusIsEquals() {
            // Given
            TodoStatus todoStatus1 = TodoStatus.TODO;
            TodoStatus todoStatus2 = TodoStatus.TODO;

            // When & Then
            assertThat(todoStatus1).isEqualTo(todoStatus2);
            assertThat(todoStatus1.hashCode()).isEqualTo(todoStatus2.hashCode());
            assertThat(todoStatus1 == todoStatus2).isTrue();
        }

        @Test
        @DisplayName("異なるステータスは等価でない")
        void differentStatusIsNotEquals() {
            // Given
            TodoStatus todoStatus1 = TodoStatus.TODO;
            TodoStatus todoStatus2 = TodoStatus.DONE;

            // When & Then
            assertThat(todoStatus1).isNotEqualTo(todoStatus2);
            assertThat(todoStatus1 != todoStatus2).isTrue();
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("toString は表示名を返す")
        void toString_returnsDisplayName() {
            // Given
            TodoStatus todoStatus1 = TodoStatus.TODO;
            TodoStatus todoStatus2 = TodoStatus.DONE;

            // When & Then
            assertThat(todoStatus1.toString()).isEqualTo(todoStatus1.getDisplayName());
            assertThat(todoStatus2.toString()).isEqualTo(todoStatus2.getDisplayName());
        }
    }
}
