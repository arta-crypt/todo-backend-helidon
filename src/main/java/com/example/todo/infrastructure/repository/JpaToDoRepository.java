package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.todo.ToDo;
import com.example.todo.domain.repository.ToDoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class JpaToDoRepository implements ToDoRepository {
    @PersistenceContext(unitName = "todoPU")
    private EntityManager em;

    @Override
    public Optional<ToDo> findById(Long id) {
        return Optional.ofNullable(em.find(ToDo.class, id));
    }

    @Override
    public List<ToDo> findAll() {
        return em.createQuery("SELECT t FROM ToDo t", ToDo.class).getResultList();
    }

    @Override
    public ToDo create(ToDo sourceTodo) {
        em.persist(sourceTodo);
        return sourceTodo;
    }

    @Override
    public ToDo update(ToDo sourceTodo) {
        Optional<ToDo> existingTodo = findById(sourceTodo.getId());
        if (existingTodo.isEmpty()) {
            throw new EntityNotFoundException("エンティティ with ID " + sourceTodo.getId() + " not found");
        }
        return em.merge(sourceTodo);
    }

    @Override
    public void delete(Long id) {
        Optional<ToDo> existingTodo = findById(id);
        if (existingTodo.isEmpty()) {
            throw new EntityNotFoundException("エンティティ with ID " + id + " not found");
        }
        em.remove(existingTodo.get());
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
