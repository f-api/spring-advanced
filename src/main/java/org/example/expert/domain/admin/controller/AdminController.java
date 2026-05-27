package org.example.expert.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.admin.AdminFacade;
import org.example.expert.domain.admin.dto.UserRoleChangeRequest;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminFacade adminFacade;

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        adminFacade.deleteComment(commentId);
    }

    @PatchMapping("/users/{userId}")
    public void changeUserRole(@PathVariable long userId,
                               @RequestBody UserRoleChangeRequest request) {
        adminFacade.changeUserRole(userId, request);
    }
}
