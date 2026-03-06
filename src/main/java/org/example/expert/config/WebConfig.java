package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    //HttpServletRequest -> AuthUser -> @Auth AuthUser authUser로 전달하는 객체 생성

    private final AuthUserArgumentResolver authUserArgumentResolver;
    private final AdminCheckInterceptor adminCheckInterceptor;

    /*
     * 4. 스프링이 컨트롤러의 파라미터를 처리할 때 사용할 '정보 리스트'에 커스텀 Resolver를 추가하는 작업
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
    }


    //인터셉터 추가 /스프링이 제공하는 레지스트리 및 작성한 adminCheckInterceptor(admin 관련 주소 전부 가져오기)를 활용
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(adminCheckInterceptor).addPathPatterns("/admin/**");


    }
}
