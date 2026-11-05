package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String oldPassword;


	@Pattern(regexp = ".*\\d.*", message = "새 비밀번호는 숫자를 포함해야 합니다.")
	@Pattern(regexp = ".*[A-Z].*", message = "새 비밀번호는 대문자를 포함해야 합니다.")
	@Size(min = 8, message = "새 비밀번호는 최소 8자 이상이어야 합니다.")
    @NotBlank(message = "새로운 비밀번호는 필수입니다.")
    private String newPassword;
}
