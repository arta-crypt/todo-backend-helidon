package com.example.todo.domain.model.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ToDoエンティティの単体テスト")
public class ToDoTest {

    @Test
    @DisplayName("コンストラクタでToDoが正しく生成されること")
    void testToDoCreation() {
        ToDo toDo = new ToDo(1L, "コーヒーを買う", false);
        assertEquals(1L, toDo.getId());
    }

    @Test
    @DisplayName("GetterおよびSetterでプロパティが正しく設定・取得できること")
    void testToDoGetterAndSetter() {
        ToDo toDo = new ToDo();
        toDo.setId(1L);
        toDo.setTitle("コーヒーを買う");
        toDo.setDone(false);
        assertAll(
                () -> assertEquals(1L, toDo.getId()),
                () -> assertFalse(toDo.isDone()),
                () -> assertEquals("コーヒーを買う", toDo.getTitle())
        );
    }


}
