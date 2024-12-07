package com.project.shopapp.filters;

import com.project.shopapp.components.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        //Allow all requests without authentication
        //filterChain.doFilter(request, response);

        if(isBypassToken(request)){
            //Allow requests without authentication
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        }
    }
    private boolean isBypassToken(@NotNull HttpServletRequest request){
        //These are the endpoints that don't need authentication(token)
        final List<Pair<String, String>> bypassTokens = List.of(
                Pair.of(apiPrefix+"/users/login", "POST"),
                Pair.of(apiPrefix+"/users/register", "POST"),
                Pair.of(apiPrefix+"/products", "GET"),
                Pair.of(apiPrefix+"/categories", "GET")
        );
        for(Pair<String, String> bypassToken:bypassTokens){
            /*
            should use getServletPath() instead of getRequestURI()
            to get the path of the request
            because:
                getRequestURI() returns the full URI of the request
                        (including protocol, host, port, context path, servlet path và query string)
                getServletPath() returns the path of the request (Ex: /api/v1/users/login)
             */
            return request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond());
        }
        return false;
    }
}
