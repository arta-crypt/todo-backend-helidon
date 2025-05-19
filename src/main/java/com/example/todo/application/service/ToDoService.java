package com.example.todo.application.service;

import com.example.todo.domain.model.todo.ToDo;

import java.util.List;
import java.util.Optional;

public interface ToDoService {
    ToDo create(ToDo todo);

    Optional<ToDo> findById(Long id);

    List<ToDo> findAll();

    ToDo update(ToDo todo);

    void delete(Long id);
}
