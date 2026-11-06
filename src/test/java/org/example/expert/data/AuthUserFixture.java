package org.example.expert.data;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;

public class AuthUserFixture {
	public static AuthUser createAuthUser() {
		return new AuthUser(1L, "user@test.com", UserRole.USER);
	}

	public static AuthUser createAdminAuthUser() {
		return new AuthUser(2L, "admin@test.com", UserRole.ADMIN);
	}

}
