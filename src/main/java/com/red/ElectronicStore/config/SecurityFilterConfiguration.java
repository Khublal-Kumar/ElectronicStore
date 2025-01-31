package com.red.ElectronicStore.config;

import com.red.ElectronicStore.Enumeration.RoleName;
import com.red.ElectronicStore.jwt.JwtAuthenticationEntryPoint;
import com.red.ElectronicStore.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfiguration {

    private final JwtAuthenticationEntryPoint point;

    private final JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .cors(cors -> cors.disable()) // Disable CORS (customize if needed)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())// Protect all other endpoints
                .exceptionHandling(e->e.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/users/image/**").permitAll() // If you want public access to images
//                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")
//                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(e -> e.authenticationEntryPoint(point))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
