package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;
    @Pattern(regexp = "^[A-Z0-9]+$", message = "새 비밀번호는 숫자와 영어 대문자를 포함해야합니다")
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야합니다")
    @NotBlank(message = "새 비밀번호는 필수 입력값입니다")
    private String newPassword;
}
