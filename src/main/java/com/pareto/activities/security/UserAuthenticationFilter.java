package com.pareto.activities.security;

import com.pareto.activities.config.Constant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws
            ServletException,
            IOException {

        String userId;
        String roles;

        try {
            userId = request.getHeader(Constant.USER_HEADER);
            roles = request.getHeader(Constant.USER_ROLES_HEADER);
        } catch (
                Exception e) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }

        if (userId == null) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }

        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (StringUtils.hasText(roles)) {
            authorities = Arrays
                    .stream(roles.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(
                request,
                response
        );
    }
}