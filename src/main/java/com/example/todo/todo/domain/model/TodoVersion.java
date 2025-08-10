package com.example.todo.todo.domain.model;

import com.example.todo.shared.domain.valueobject.Version;

public class TodoVersion extends Version<Long> {

    /**
     * コンストラクタ
     *
     * @param value Versionの実際の値
     */
    public TodoVersion(Long value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TodoVersion increment() {
        return new TodoVersion(getValue() + 1L);
    }
}


