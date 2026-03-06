package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {


    //목록조회
    // todo entity 불러오기      // todo와 관련있는 유저정보 불러오고 수정시간 내림차순으로 정렬하기

    // "LEFT JOIN FETCH t.user u" fetch join문을 entityGraph로 변경 : user table 조회 후 Mapping
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC")
    // page 형태로
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);


    // 상세조회
    // todo entity 불러오기      // todo와 관련있는 유저정보 불러오는데 특정id에 관한것만 불러오기

    // "LEFT JOIN FETCH t.user "  fetch join문을 entityGraph로 변경 : user table 조회 후 id Mapping
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Todo t " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}
