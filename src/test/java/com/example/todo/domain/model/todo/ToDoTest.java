package com.example.todo.domain.model.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ToDoエンティティの単体テスト")
public class ToDoTest {

    /**
     * テスト用の「やること」タスクID
     */
    private static final Long TEST_ID = 1L;

    /**
     * テスト用の「やること」タスクタイトル
     */
    private static final String TEST_TITLE = "コーヒーを買う";

    /**
     * テスト用の「やること」タスク完了状態
     */
    private static final boolean TEST_DONE = false;

    @ParameterizedTest
    @CsvSource(value = {
            "1,コーヒーを買う,false",
            "\\u0000,テストを書く,false"
    }, nullValues = "\\u0000")
    @DisplayName("コンストラクタでToDoが正しく生成されること")
    void testToDoCreation(Long id, String title, boolean done) {
        // 実行
        ToDo toDo = new ToDo(id, title, done);

        // 検証
        verifyTaskProperties(toDo, id, title, done);
    }

    @Test
    @DisplayName("GetterおよびSetterでプロパティが正しく設定・取得できること")
    void testToDoGetterAndSetter() {
        // 準備
        Long newId = 2L;
        String newTitle = "コーヒーを飲む";
        boolean newDone = true;
        ToDo toDo = new ToDo(TEST_ID, TEST_TITLE, TEST_DONE);

        // 実行
        toDo.setId(newId);
        toDo.setTitle(newTitle);
        toDo.setDone(newDone);

        // 検証
        verifyTaskProperties(toDo, newId, newTitle, newDone);
    }

    /**
     * 「やること」タスクエンティティのプロパティが期待値と一致するかを検証します。
     * 各プロパティ（ID、タイトル、完了状態）について個別に検証を行います。
     *
     * @param actual        検証対象の「やること」タスクエンティティ
     * @param expectedId    期待されるID値
     * @param expectedTitle 期待されるタイトル
     * @param expectedDone  期待される完了状態
     */
    void verifyTaskProperties(ToDo actual, Long expectedId, String expectedTitle, boolean expectedDone) {
        assertAll(
                () -> assertEquals(expectedId, actual.getId(), "IDが期待値と一致しません"),
                () -> assertEquals(expectedDone, actual.isDone(), "完了状態が期待値と一致しません"),
                () -> assertEquals(expectedTitle, actual.getTitle(), "タイトルが期待値と一致しません")
        );
    }
}
