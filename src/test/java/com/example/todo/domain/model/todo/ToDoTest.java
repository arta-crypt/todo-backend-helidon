package com.example.todo.domain.model.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    /**
     * テスト用の「やること」タスクインスタンス
     */
    private ToDo testToDo;

    @BeforeEach
    void setUp() {
        testToDo = new ToDo(TEST_ID, TEST_TITLE, TEST_DONE);
    }

    @Test
    @DisplayName("コンストラクタでToDoが正しく生成されること")
    void testToDoCreation() {
        // 検証
        verifyTaskProperties(testToDo, TEST_ID, TEST_TITLE, TEST_DONE);
    }

    @Test
    @DisplayName("GetterおよびSetterでプロパティが正しく設定・取得できること")
    void testToDoGetterAndSetter() {
        // 準備
        Long newId = 2L;
        String newTitle = "コーヒーを飲む";
        boolean newDone = true;

        // 実行
        testToDo.setId(newId);
        testToDo.setTitle(newTitle);
        testToDo.setDone(newDone);

        // 検証
        verifyTaskProperties(testToDo, newId, newTitle, newDone);
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
