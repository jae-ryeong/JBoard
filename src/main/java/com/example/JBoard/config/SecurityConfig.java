package com.example.JBoard.config;

import com.example.JBoard.Repository.UserAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {    // login 페이지가 뜨지 않게 해준다.
        return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin().and()
                .build();
    }*/

/*    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/")).permitAll()).build();
    }*/

/*    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // 스프링부트 3.1부터 이렇게 표현하라고 한다.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher(PathRequest.toStaticResources().atCommonLocations().toString()))
                        .permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/","/articles"))
                                .permitAll()
                        .anyRequest().authenticated()   // 위에를 제외한 나머지 request는 인증이 되어야만 한다.
                )
                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(new AntPathRequestMatcher("/detail/**"))
                                .authenticated()
                                .anyRequest().permitAll())  // 목록은 볼 수 있지만, 상세글은 로그인해야 볼 수 있다.
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")  // 이 페이지에서 로그인기능 처리 권한을 넘기겠다??와 비슷한 의미, form의 action태그와 경로가 일치해야한다.
                        .usernameParameter("uid")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            System.out.println("authentication : " + authentication.getName());
                            response.sendRedirect("/boardlist");
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/user/login");
                        })
                        .permitAll())
                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/user/logout")
                                .logoutSuccessUrl("/boardlist")
                )
                .build();   // 로그인 페이지는 무조건 접근 가능하게.

    }

/*    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }*/

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {  // 패스워드 암호화
        return new BCryptPasswordEncoder();
    }
}
