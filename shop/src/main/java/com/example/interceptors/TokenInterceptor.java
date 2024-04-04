package com.example.interceptors;

import com.example.pojo.Customer;
import com.example.utils.JwtUtil;
import com.example.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.constant.Constant.CUSTOMER_KEY;

@Component
public class TokenInterceptor extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

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
            String redisToken = redisUtil.get(CUSTOMER_KEY + username);
            if (redisToken == null || !redisToken.equals(token)) {
                throw new RuntimeException();
            }
            Customer customer = new Customer();
            customer.setId(id);
            customer.setUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken.authenticated(customer, null, customer.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            //设置响应状态码为401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
