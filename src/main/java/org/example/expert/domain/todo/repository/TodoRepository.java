package org.example.expert.domain.todo.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Optional<Todo> findById(Long todoId);  // ✅ 기존 findByIdWithUser를 대체함
}

