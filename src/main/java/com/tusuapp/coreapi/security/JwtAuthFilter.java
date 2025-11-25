package com.tusuapp.coreapi.security;


import com.tusuapp.coreapi.services.auth.AdminInfoService;
import com.tusuapp.coreapi.services.auth.JwtService;
import com.tusuapp.coreapi.services.auth.UserInfoService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserInfoService userDetailsService;
    private final JwtService jwtService;
    private final AdminInfoService adminInfoService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/admin")) {
            doFilterAdminInternal(request, response, filterChain);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;


        if (authHeader != null && authHeader.startsWith("Bearer ")
                && !request.getRequestURI().endsWith("/login")
                && !request.getRequestURI().endsWith("/register")) {
            token = authHeader.substring(7);

            try {
                username = jwtService.extractUsername(token);
            } catch (ExpiredJwtException e) {
                System.out.println("JWT expired: " + e.getMessage());
                response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token has expired. Please log in again.\"}");
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }


    protected void doFilterAdminInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")
                && !request.getRequestURI().endsWith("/login")) {
            token = authHeader.substring(7);
            System.out.println("Checking token " + token);
            try {
                username = jwtService.extractEmail(token);
            }catch (ExpiredJwtException e){
                System.out.println("JWT expired: " + e.getMessage());
                response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token has expired. Please log in again.\"}");
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = adminInfoService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}