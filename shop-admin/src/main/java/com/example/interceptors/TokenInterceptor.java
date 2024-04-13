package com.example.interceptors;

import com.example.pojo.User;
import com.example.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class TokenInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        //从请求头获取token
        String token = request.getHeader("Authorization");

        //token为空
        if (!StringUtils.hasLength(token)) {
            doFilter(request, response, filterChain);
            return;
        }

        //验证token
        Claims claims;
        try {
            claims = JwtUtil.parseToken(token);
        } catch (Exception e) {
            doFilter(request, response, filterChain);
            return;
        }
        try {
            Long id = claims.get("id", Long.class);
            String username = claims.get("username", String.class);
            List<String> premissions = claims.get("permissions", ArrayList.class);
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPermissions(premissions);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            //设置响应状态码为401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
