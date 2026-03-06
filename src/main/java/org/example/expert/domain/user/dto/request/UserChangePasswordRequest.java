package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;


    //service에 불필요한 if문 제거 후 size와 pattern 어노테이션 사용 후 제약조건 설정\
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
    //if (userChangePasswordRequest.getNewPassword().length() < 8 ||
    @Pattern(regexp = ".*\\d.*", message = "숫자를 포함해야 합니다.")
    //!userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
    @Pattern(regexp = ".*[A-Z].*", message = "대문자를 포함해야 합니다.")
    //!userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*"))
    @NotBlank
    private String newPassword;
}


