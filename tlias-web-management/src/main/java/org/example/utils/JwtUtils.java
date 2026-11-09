package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 生成 Token（登录用） / 解析 Token（拦截器验票用）
 */
public class JwtUtils {

    // 密钥：至少 32 位，正式项目换成随机字符串，别用测试那个
    public static final String SECRET_KEY = "tlias-secret-key-2024-must-be-long-enough-32";

    // 过期时间：12 小时（单位毫秒）
    static final long EXPIRE_TIME = 12 * 60 * 60 * 1000L;

    /**
     * 生成 JWT 令牌
     * @param claims 要存入 Token 的自定义数据（如 id、username）
     * @return Token 字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)   // 签名算法 + 密钥
                .addClaims(claims)                                 // 塞入自定义数据
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME)) // 过期时间
                .compact();                                        // 生成最终字符串
    }

    /**
     * 解析 JWT 令牌
     * 解析成功返回 Claims（可从中取 id、username 等）
     * 解析失败（过期 / 篡改 / 格式错误）会抛异常，调用方 try-catch 处理
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)   // 用同一把密钥验签
                .parseClaimsJws(token)       // 解析
                .getBody();                  // 取出 Payload 里的 Claims
    }
}