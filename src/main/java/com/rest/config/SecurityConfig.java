package com.rest.config;

import com.rest.service.implementations.UserInfoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(configurer ->
                        configurer
                                .requestMatchers("/api/my-drafts", "/api/my-posts").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/posts").hasAnyRole("ADMIN", "AUTHOR")
                                .requestMatchers(HttpMethod.PUT, "/api/posts" , "/api/comment").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/posts/{postId}",
                                                    "/api/comment/{commentId}").hasAnyRole("ADMIN", "AUTHOR")
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
        )
                .logout(logout -> logout.permitAll())
                .csrf().disable()
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}