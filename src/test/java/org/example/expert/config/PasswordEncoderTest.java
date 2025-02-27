package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드야_정상적으로_동작해() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword); // 비밀번호 암호화

        // when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword); // ✅ rawPassword가 첫 번째 인자

        // then
        assertTrue(matches);
    }
}

//rawPassword를 첫 번째 인자로 전달해야 하고 encodedPassword를 두 번째 인자로 전달해야 함.