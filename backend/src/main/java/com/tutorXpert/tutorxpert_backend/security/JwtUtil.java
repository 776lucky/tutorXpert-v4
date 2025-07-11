package com.tutorXpert.tutorxpert_backend.security;

import cn.hutool.jwt.Claims;
import com.tutorXpert.tutorxpert_backend.config.JwtConfig;
import org.springframework.stereotype.Component;
import cn.hutool.jwt.JWT;


@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // 生成 Token
    public String generateToken(String username, Long userId) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .setPayload("userId", userId)
                .setPayload("username", username)
                .setPayload("iat", now)
                .setPayload("exp", now + jwtConfig.getExpiration())
                .setKey(jwtConfig.getSecret().getBytes())
                .sign();
    }




    // 验证 Token
    public String validateToken(String token) {
        JWT jwt = JWT.of(token).setKey(jwtConfig.getSecret().getBytes());
        Object expObj = jwt.getPayload("exp");
        long exp = expObj instanceof Number ? ((Number) expObj).longValue() : Long.parseLong(expObj.toString());
        if (!jwt.verify() || exp < System.currentTimeMillis()) {
            return null;
        }

        return jwt.getPayload("username").toString();
    }


    // 提取 userId
    public Integer getUserIdFromToken(String token) {
        JWT jwt = JWT.of(token).setKey(jwtConfig.getSecret().getBytes());
        if (!jwt.verify()) {
            throw new RuntimeException("Invalid JWT token");
        }

        Object userIdObj = jwt.getPayload("userId");
        if (userIdObj == null) {
            throw new RuntimeException("userId not found in token");
        }

        return Integer.parseInt(userIdObj.toString());
    }
}

