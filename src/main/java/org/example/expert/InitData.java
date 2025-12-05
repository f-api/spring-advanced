package org.example.expert;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final CommentRepository commentRepository;

    @PostConstruct
    public void init() {

        String encodedPassword = passwordEncoder.encode("TestTest1");

        UserRole userRole1 = UserRole.of("ADMIN");
        UserRole userRole2 = UserRole.of("USER");

        User newUser = new User(
                "lee@seo.jun",
                encodedPassword,
                userRole1
        );

        User newUser1 = new User(
                "lee@jun.beum",
                encodedPassword,
                userRole2
        );

        User savedUser = userRepository.save(newUser);
        User savedUser1 = userRepository.save(newUser1);


        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                "TODO 테스트",
                "TODO 내용인가요",
                weather,
                savedUser
        );

        Todo newTodo2 = new Todo(
                "TODO 테스트",
                "TODO 내용인가요",
                weather,
                savedUser
        );

        Todo newTodo3 = new Todo(
                "TODO 테스트",
                "TODO 내용인가요",
                weather,
                savedUser1
        );
        Todo newTodo4 = new Todo(
                "TODO 테스트",
                "TODO 내용인가요",
                weather,
                savedUser1
        );
        Todo newTodo5 = new Todo(
                "TODO 테스트",
                "TODO 내용인가요",
                weather,
                savedUser
        );

        Todo savedTodo = todoRepository.save(newTodo);
        Todo savedTodo1 = todoRepository.save(newTodo2);
        Todo savedTodo2 = todoRepository.save(newTodo3);
        Todo savedTodo3 = todoRepository.save(newTodo4);
        Todo savedTodo4 = todoRepository.save(newTodo5);

        Comment newComment = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo
        );
        Comment newComment1 = new Comment(
                "댓글 내용입니다.",
                newUser1,
                savedTodo
        );Comment newComment2 = new Comment(
                "댓글 내용입니다.",
                newUser1,
                savedTodo
        );Comment newComment3 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo2
        );Comment newComment4 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo3
        );Comment newComment5 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo4
        );Comment newComment6 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo1
        );Comment newComment7 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo
        );Comment newComment8 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo
        );Comment newComment9 = new Comment(
                "댓글 내용입니다.",
                newUser,
                savedTodo
        );

        Comment savedComment = commentRepository.save(newComment);
        Comment savedComment1 = commentRepository.save(newComment1);
        Comment savedComment2 = commentRepository.save(newComment2);
        Comment savedComment3 = commentRepository.save(newComment3);
        Comment savedComment4 = commentRepository.save(newComment4);
        Comment savedComment5 = commentRepository.save(newComment5);
        Comment savedComment6 = commentRepository.save(newComment6);
        Comment savedComment7 = commentRepository.save(newComment7);
        Comment savedComment8 = commentRepository.save(newComment8);
        Comment savedComment9 = commentRepository.save(newComment9);

    }
}