package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import org.example.expert.domain.user.entity.User;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void 사용자_정보_및_todo리스트_설정() {
        // given
        testUser = userRepository.save(new User("test@example.com", "password", UserRole.USER));

        todoRepository.save(new Todo("test 1", "contents 1", "맑음", testUser));
        todoRepository.save(new Todo("test 2", "contents 2", "흐림", testUser));
    }


    @Test
    @DisplayName("N+1 문제 없이 모든 Todo를 조회") //N+1을 입력하기위해서는...
    void 문제_없이_모든_Todo를_조회() {
        // when
        List<Todo> todos = todoRepository.findAll();

        for (Todo todo : todos) {
            System.out.println(todo.getUser().getEmail());
        }

        // then
        assertThat(todos).isNotEmpty();
        assertThat(todos).hasSize(2);
        assertThat(todos.get(0).getUser()).isNotNull();
        assertThat(todos.get(0).getUser().getEmail()).isEqualTo("test@example.com");
    }

}
