package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Optional<Todo> findById(Long id);

    int countById(Long todoId);
}
