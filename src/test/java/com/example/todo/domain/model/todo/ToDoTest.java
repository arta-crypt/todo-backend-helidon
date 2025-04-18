package com.example.todo.domain.model.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ToDoエンティティの単体テスト")
public class ToDoTest {

    @ParameterizedTest
    @CsvSource(value = {
            "0,コーヒーを買う,false",
            "\\u0000,テストを書く,false"
    }, nullValues = "\\u0000")
    @DisplayName("基本コンストラクタでToDoが正しく生成されること")
    void testToDoCreation(Long id, String title, boolean done) {
        // 実行
        ToDo toDo = new ToDo(id, title, done);

        // 検証
        verifyBasicTaskProperties(toDo, id, title, done);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,コーヒーを買う,false,0,2022-01-01T00:00:00,2022-01-01T00:00:00",
            "\\u0000,\\u0000,false,\\u0000,\\u0000,\\u0000",
            "-9223372036854775808,,false,-9223372036854775808,1968-01-01T00:00:00,1968-01-01T00:00:00",
            "9223372036854775807,あああああああああああ,true,9223372036854775807,9999-01-01T00:00:00,9999-01-01T00:00:00",
    }, nullValues = "\\u0000")
    @DisplayName("拡張コンストラクタで「やること」タスクが正しく生成されること")
    void testToDoExtendedConstructor(Long id, String title, boolean done, Long version, LocalDateTime createdAt, LocalDateTime updatedAt) {
        // 実行
        ToDo toDo = new ToDo(id, title, done, version, createdAt, updatedAt);

        // 検証
        verifyAllTaskProperties(toDo, id, title, done, version, createdAt, updatedAt);
    }

    @Test
    @DisplayName("GetterおよびSetterでプロパティが正しく設定・取得できること")
    void testToDoGetterAndSetter() {
        // 準備
        Long id = 2L;
        String title = "コーヒーを飲む";
        boolean done = true;
        Long version = 3L;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now();
        ToDo toDo = new ToDo();

        // 実行
        toDo.setId(id).setTitle(title).setDone(done).setVersion(version).setCreatedAt(createdAt).setUpdatedAt(updatedAt);

        // 検証
        verifyAllTaskProperties(toDo, id, title, done, version, createdAt, updatedAt);
    }

    @Test
    @DisplayName("setTitleおよびsetDoneで更新日時が自動的に更新されること")
    void testToDoSetTitleAndSetDone() {
        // 準備
        ToDo toDo = new ToDo(0L, "初期タイトル", false);
        LocalDateTime initialUpdatedAt = toDo.getUpdatedAt();

        // 少し待つ（タイムスタンプに差が出るようにするため）
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // 例外は無視
        }

        // タイトル更新の実行と検証
        toDo.setTitle("新しいタイトル");
        assertTrue(toDo.getUpdatedAt().isAfter(initialUpdatedAt));

        // 更新日時を記録
        LocalDateTime afterTitleUpdatedAt = toDo.getUpdatedAt();

        // 再び少し待つ
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // 例外は無視
        }

        // 完了状態更新の実行と検証
        toDo.setDone(true);
        assertTrue(toDo.getUpdatedAt().isAfter(afterTitleUpdatedAt));
    }

    @Test
    @DisplayName("コピーコンストラクタで「やること」タスクが正しくコピーされること")
    void testToDoCopyConstructor() {
        // 準備
        Long originalId = Long.MAX_VALUE;
        String originalTitle = "ああああああああああああああああああああああああああああああああ";
        boolean originalDone = false;
        Long originalVersion = Long.MAX_VALUE;
        LocalDateTime originalCreatedAt = LocalDateTime.now();
        LocalDateTime originalUpdatedAt = LocalDateTime.now();
        ToDo originalToDo = new ToDo(originalId, originalTitle, originalDone, originalVersion, originalCreatedAt, originalUpdatedAt);

        // 実行
        ToDo copiedToDo = new ToDo(originalToDo);

        // 検証
        verifyAllTaskProperties(copiedToDo, originalId, originalTitle, originalDone, originalVersion, originalCreatedAt, originalUpdatedAt);
    }

    @Test
    @DisplayName("コピーコンストラクタにnullを渡すとNullPointerExceptionが発生すること")
    void testToDoCopyConstructorWithNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ToDo(null),
                "nullを渡す場合はNullPointerExceptionが発生するはずです");

        assertEquals("コピー元のオブジェクトがnullです", exception.getMessage());
    }

    /**
     * 「やること」タスクエンティティの基本プロパティが期待値と一致するかを検証します。
     * 各プロパティ（ID、タイトル、完了状態）について個別に検証を行います。
     *
     * @param actual        検証対象の「やること」タスクエンティティ
     * @param expectedId    期待されるID値
     * @param expectedTitle 期待されるタイトル
     * @param expectedDone  期待される完了状態
     */
    void verifyBasicTaskProperties(ToDo actual, Long expectedId, String expectedTitle, boolean expectedDone) {
        assertAll(
                () -> assertEquals(expectedId, actual.getId(), "IDが期待値と一致しません"),
                () -> assertEquals(expectedDone, actual.isDone(), "完了状態が期待値と一致しません"),
                () -> assertEquals(expectedTitle, actual.getTitle(), "タイトルが期待値と一致しません")
        );
    }

    /**
     * 「やること」タスクエンティティのすべてのプロパティが期待値と一致することを検証する
     *
     * @param actual            検証対象の「やること」タスクエンティティ
     * @param expectedId        期待されるID値
     * @param expectedTitle     期待されるタイトル
     * @param expectedDone      期待される完了状態
     * @param expectedVersion   期待されるバージョン値
     * @param expectedCreatedAt 期待される作成日時
     * @param expectedUpdatedAt 期待される更新日時
     */
    void verifyAllTaskProperties(ToDo actual, Long expectedId, String expectedTitle, boolean expectedDone,
                                 Long expectedVersion, LocalDateTime expectedCreatedAt, LocalDateTime expectedUpdatedAt) {
        verifyBasicTaskProperties(actual, expectedId, expectedTitle, expectedDone);
        assertAll(
                () -> assertEquals(expectedVersion, actual.getVersion(), "バージョンが期待値と一致しません"),
                () -> assertEquals(expectedCreatedAt, actual.getCreatedAt(), "作成日時が期待値と一致しません"),
                () -> assertEquals(expectedUpdatedAt, actual.getUpdatedAt(), "更新日時が期待値と一致しません")
        );
    }
}
