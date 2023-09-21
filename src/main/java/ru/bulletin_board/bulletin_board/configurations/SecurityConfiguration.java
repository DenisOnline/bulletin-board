package ru.bulletin_board.bulletin_board.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.bulletin_board.bulletin_board.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/";
    public static final String PRODUCTS_URL = "/product/**";
    public static final String IMAGES_URL = "/images/**";
    public static final String REGISTRATION_URL = "/registration";
    public static final String USERNAME_PARAMETER = "email";
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(DEFAULT_SUCCESS_URL, PRODUCTS_URL, IMAGES_URL, REGISTRATION_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage(LOGIN_URL)
                        .usernameParameter(USERNAME_PARAMETER)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}