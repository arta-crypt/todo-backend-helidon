package com.example.todo.testutil.builder;

import com.example.todo.domain.model.todo.ToDo;

/**
 * 「やること」タスクエンティティのテストデータビルダー
 */
public class ToDoBuilder {
    private Long id;
    private String title = "やることサンプル";
    private boolean done = false;

    public static ToDo sample() {
        return new ToDoBuilder().build();
    }

    public static ToDo completed() {
        return new ToDoBuilder().withDone(true).build();
    }

    public ToDoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ToDoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ToDoBuilder withDone(boolean done) {
        this.done = done;
        return this;
    }

    public ToDo build() {
        return new ToDo(id, title, done);
    }
}
