package com.example.JBoard.config;

import com.example.JBoard.jwt.filter.JwtAuthenticationFilter;
import com.example.JBoard.jwt.filter.JwtTokenFilter;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;

    private static String secretKey = "my-secret-key-123123";

    /*private final UserSecurityService loginService;
    private final JwtService jwtService;
    private final UserAccountRepository userRepository;
    private final ObjectMapper objectMapper;*/

/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {    // 세션으로 로그인 처리
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/detail/**")).authenticated()
                                )  // 목록은 볼 수 있지만, 상세글은 로그인해야 볼 수 있다.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/boardCreateForm")).authenticated() // 로그인을 해야 게시글 작성 가능
                                .anyRequest().permitAll())
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
    }*/

    @Bean   // JWT를 사용하여 로그인
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable) // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session // 상태 비저장이므로 요청이 있을 때마다 사용자를 다시 인증
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/detail/**")).authenticated())  // 목록은 볼 수 있지만, 상세글은 로그인해야 볼 수 있다.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN"))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/jwt-login/info")).authenticated())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                new AntPathRequestMatcher("/boardCreateForm")).authenticated() // 로그인을 해야 게시글 작성 가능
                                .anyRequest().permitAll())
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new JwtTokenFilter(userService, secretKey))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

   @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {  // 패스워드 암호화
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // FormLogin 사용 X
                .headers(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session // 상태 비저장이므로 요청이 있을 때마다 사용자를 다시 인증
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class).
        addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class).build();

        //return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */


/*    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }*/

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
/*    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    *//**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     *//*
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    *//**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     *//*

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }*/

}

