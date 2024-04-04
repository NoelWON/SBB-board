package com.noelwon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경 설정 파일임을 의미하는 애너테이션
@EnableWebSecurity  // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 하는 것
@EnableMethodSecurity(prePostEnabled = true) // 로그인 여부 판별 하는 것
public class SecurityConfig {
	// 스프링 시큐리티의 세부 설정은 @Bean 애너테이션을 통해 SecurityFilterChain 빈을 생성하여 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
            		// new AntPathRequestMatcher("/**") 인증되지 않은 모든 페이지의 요청을 허락한다는 것
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            // h2 콘솔관련 csrf 예외처리 부분 
            .csrf((csrf) -> csrf
            		.ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
//                    .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
//            .headers((headers) -> headers
//                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            // 로그인 설정을 담당하는 부분 로그인 페이지, 로그인시 이동할 페이지로 구성됨
            .formLogin((formLogin) -> formLogin
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/"))
            // 로그아웃 설정을 담당하는 부분
            .logout((logout) -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true))
        ;
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // UserSecurityService에서 사용하기 위해서 추가한 Been
    // 스프링 시큐리티의 인증을 처리한다.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
