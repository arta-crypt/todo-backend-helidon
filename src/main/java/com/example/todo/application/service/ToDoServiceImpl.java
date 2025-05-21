package com.example.todo.application.service;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

public class ToDoServiceImpl implements ToDoService {
    @Inject
    ToDoRepository repository;

    @Override
    public ToDo create(ToDo todo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ToDo> findById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ToDo> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ToDo update(ToDo todo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }
}
