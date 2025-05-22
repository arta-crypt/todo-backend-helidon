package com.example.todo.application.service;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.infrastructure.repository.JpaToDoRepository;
import com.example.todo.testutil.builder.ToDoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToDoServiceImplTest {
    private JpaToDoRepository repository;
    private ToDoServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(JpaToDoRepository.class);
        service = new ToDoServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        // Arrange
        var now = LocalDateTime.now();
        ToDo todo1 = new ToDoBuilder().withTitle("Task1").withCreatedAt(now).withUpdatedAt(now).build();
        ToDo todo2 = new ToDoBuilder().withTitle("Task2").withCreatedAt(now).withUpdatedAt(now).build();
        when(repository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // Act
        List<ToDo> result = service.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Task1", result.getFirst().getTitle()),
                () -> assertEquals("Task2", result.get(1).getTitle())
        );
    }
}