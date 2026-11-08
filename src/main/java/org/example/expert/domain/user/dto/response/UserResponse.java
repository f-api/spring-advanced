package org.example.expert.domain.user.dto.response;

import org.example.expert.domain.user.enums.UserRole;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
	private final UserRole userRole;

    public UserResponse(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
		this.userRole = userRole;
    }
}
