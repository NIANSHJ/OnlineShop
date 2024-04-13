package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static com.example.constant.Constant.LOGIN_EXPIRE;

public class JwtUtil {

    private static final String SECRET = "NIANSHAOHUAIJIUXIUXIANYANGSHENGYIBANXIANCHONGZAIZHAOGONGZUO";

    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;

    //接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return Jwts.builder().claims(claims).issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(LOGIN_EXPIRE)))
                .signWith(SECRET_KEY, ALGORITHM).compact();
    }

    //接收token,验证token,并返回业务数据
    public static Claims parseToken(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token)
                .getPayload();
    }

}
