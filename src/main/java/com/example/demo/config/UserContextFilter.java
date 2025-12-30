package com.example.demo.config;


import com.example.demo.context.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                UserContext context = new UserContext();
                context.setUsername(authentication.getName());
                // For now, set a default userId since we're using in-memory auth
                context.setUserId(1L);  // You can change this logic later

                UserContext.setCurrentUser(context);
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}

