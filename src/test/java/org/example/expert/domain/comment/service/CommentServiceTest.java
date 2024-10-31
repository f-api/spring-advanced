package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.todo.util.TodoUtil;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private TodoUtil todoUtil;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void comment_등록_중_할_일을_찾지_못해_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.USER);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("Test comment");
        long todoId = 1;

        /* commentService.saveComment가 진행되면서 예외처리를 제대로 하는지 알아보기 위한 테스트 입니다.
        진행 과정에서 findById가 호출되고 이를 위해 DB와 연결되어야 하는데 테스트이기 때문에 할 수 없습니다.
        반환될 객체가 필요한 게 예외 처리를 확인하는 것이기 때문에 when - thenThrow 사용했습니다. */
        when(todoUtil.findById(todoId)).thenThrow(new InvalidRequestException("Todo not found"));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentService.saveComment(authUser, todoId, commentSaveRequest);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    public void comment를_정상적으로_등록한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo("title", "contents", "weather", user);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("contents");
        long todoId = 1L;

        /* commentService.saveComment가 진행되며 commentRepository.save를 호출합니다.
        이를 위해 DB와 연결되어야 하는데 테스트이기 때문에 할 수 없습니다.
        Mock 객체, 반환될 객체가 필요하기 때문에 given - willReturn 사용했습니다. */
        Comment newComment = new Comment("contents", user, todo);
        given(commentRepository.save(any())).willReturn(newComment);

        given(todoUtil.findById(todoId)).willReturn(todo);

        // when
        CommentSaveResponse result = commentService.saveComment(authUser, todoId, commentSaveRequest);

        // then
        assertNotNull(result);
    }
}
