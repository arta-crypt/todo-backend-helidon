package com.example.todo.todo.domain.model;

import com.example.todo.shared.domain.valueobject.Id;

public class TodoId extends Id<Long> {

    /**
     * 指定された値でIDを構成します
     *
     * @param value 　IDの値。nullであってはならない。
     * @throws NullPointerException valueがnullの場合
     */
    public TodoId(Long value) {
        super(value);
    }
}
