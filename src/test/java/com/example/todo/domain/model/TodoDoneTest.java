package com.example.todo.domain.model;

import com.example.todo.todo.domain.model.TodoDone;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("TodoDone のテスト")
public class TodoDoneTest {

    @Nested
    @DisplayName("ステータス作成")
    class DoneCreation {

        @Test
        @DisplayName("TODOステータスを作成できる")
        void createTodoStatus() {
            // Given
            TodoDone todoDone = TodoDone.TODO;

            // Then
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(todoDone.getDisplayName()).isEqualTo("TODO");
                        softAssertions.assertThat(todoDone.isDone()).isFalse();
                    }
            );
        }

        @Test
        @DisplayName("DONEステータスを作成できる")
        void createDoneStatus() {
            // Given
            TodoDone todoDone = TodoDone.DONE;

            // Then
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(todoDone.getDisplayName()).isEqualTo("DONE");
                        softAssertions.assertThat(todoDone.isDone()).isTrue();
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
            TodoDone todoDone1 = TodoDone.TODO;
            TodoDone todoDone2 = TodoDone.TODO;

            // When & Then
            assertThat(todoDone1).isEqualTo(todoDone2);
            assertThat(todoDone1.hashCode()).isEqualTo(todoDone2.hashCode());
            assertThat(todoDone1 == todoDone2).isTrue();
        }

        @Test
        @DisplayName("異なるステータスは等価でない")
        void differentStatusIsNotEquals() {
            // Given
            TodoDone todoDone1 = TodoDone.TODO;
            TodoDone todoDone2 = TodoDone.DONE;

            // When & Then
            assertThat(todoDone1).isNotEqualTo(todoDone2);
            assertThat(todoDone1 != todoDone2).isTrue();
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("toString は表示名を返す")
        void toString_returnsDisplayName() {
            // Given
            TodoDone todoDone1 = TodoDone.TODO;
            TodoDone todoDone2 = TodoDone.DONE;

            // When & Then
            assertThat(todoDone1.toString()).isEqualTo(todoDone1.getDisplayName());
            assertThat(todoDone2.toString()).isEqualTo(todoDone2.getDisplayName());
        }
    }
}
