package org.example.expert.domain.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserChangePasswordRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_비밀번호_요청이면_검증을_통과한다() {
        // Given
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword1", "NewPassword1");

        // When
        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void 새_비밀번호가_8자_미만이면_예외가_발생한다() {
        // Given
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword1", "Ab1");

        // When
        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void 새_비밀번호에_숫자가_없으면_예외가_발생한다() {
        // Given
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword1", "NewPassword");

        // When
        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void 새_비밀번호에_대문자가_없으면_예외가_발생한다() {
        // Given
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword1", "newpassword1");

        // When
        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void 기존_비밀번호가_비어있으면_예외가_발생한다() {
        // Given
        UserChangePasswordRequest request = new UserChangePasswordRequest("", "NewPassword1");

        // When
        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
    }
}

