package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Used to define the class as a configuration class.
/*
Annotation này được sử dụng để kích hoạt
bảo mật ở cấp độ phương thức trong ứng dụng Spring Boot.
you can use @Secured, @PreAuthorize, và @PostAuthorize to control access to methods.
 */
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    //Bean annotation is used to declare a Spring bean in Spring IOC container.
    @Bean
    //nhiệm vụ như 1 bảo vệ nếu request được gửi đến thì ổng sẽ kiểm tra đủ giấy tờ chưa
    // quyền gì mà vào hệ thống
    /*
    Define a filter chain that is responsible for all security (web) concerns.
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

//         Tắt tính năng CSRF (Cross-Site Request Forgery) trong Spring Security.
        http.csrf(AbstractHttpConfigurer::disable)
                //add a filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //config rules for authorize requests
                .authorizeHttpRequests(requests->{
                    requests.requestMatchers("**")
                            //allow all requests without authentication
                            .permitAll();

                });
        //build the http security configuration and return it
        return http.build();
    }
}
