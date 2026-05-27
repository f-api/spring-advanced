package org.example.expert.domain.admin;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.admin.service.CommentAdminService;
import org.example.expert.domain.admin.dto.UserRoleChangeRequest;
import org.example.expert.domain.admin.service.UserAdminService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {

    private final CommentAdminService commentAdminService;
    private final UserAdminService userAdminService;

    public void deleteComment(long commentId) {
        commentAdminService.deleteComment(commentId);
    }

    public void changeUserRole(long userId, UserRoleChangeRequest request) {
        userAdminService.changeUserRole(userId, request);
    }

}
