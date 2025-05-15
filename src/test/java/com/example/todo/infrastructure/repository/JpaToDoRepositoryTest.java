package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.testutil.builder.ToDoBuilder;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@HelidonTest
class JpaToDoRepositoryTest {

    @Inject
    private JpaToDoRepository repo;

    @Test
    @DisplayName("JPAリポジトリでToDoを保存して取得")
    void testJpaPersist() {
        ToDo todo = new ToDoBuilder().withTitle("コーヒーを買う").withDone(false).build();
        ToDo createdTodo = repo.create(todo);

        assertNotNull(createdTodo.getId());
        Optional<ToDo> found = repo.findById(createdTodo.getId());
        assertTrue(found.isPresent());
        assertEquals("コーヒーを買う", found.get().getTitle());
    }
}