package org.example.expert.data;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;

public class UserFixture {
	public static User createUser(){
		return new User("user@test.com", "password", UserRole.USER);
	}
}
