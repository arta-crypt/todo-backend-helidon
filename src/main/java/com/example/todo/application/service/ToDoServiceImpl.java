package com.example.todo.application.service;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository repository;

    @Inject
    public ToDoServiceImpl(ToDoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ToDo create(ToDo todo) {
        return repository.create(todo);
    }

    @Override
    public Optional<ToDo> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ToDo> findAll() {
        return repository.findAll();
    }

    @Override
    public ToDo update(ToDo todo) {
        return repository.update(todo);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
