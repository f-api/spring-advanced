package org.example.expert.domain.comment.service;

import org.example.expert.data.AuthUserFixture;
import org.example.expert.data.TodoFixture;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
	@DisplayName("존재하지 않는 Todo에 댓글 등록 시 예외가 발생한다.")
    public void comment_등록_중_할일을_찾지_못해_에러가_발생한다() {
        // given
        CommentSaveRequest request = new CommentSaveRequest("contents");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentService.saveComment(authUser, 1L, request);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
	@DisplayName("존재하는 Todo에 댓글 등록 시 CommentSaveResponse를 반환한다.")
    public void comment를_정상적으로_등록한다() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        User user = User.fromAuthUser(authUser);
        Todo todo = TodoFixture.createTodo(user);

		CommentSaveRequest request = new CommentSaveRequest("contents");
        Comment comment = new Comment(request.getContents(), user, todo);

        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        CommentSaveResponse result = commentService.saveComment(authUser, todo.getId(), request);

        // then
        assertNotNull(result);
    }
}
