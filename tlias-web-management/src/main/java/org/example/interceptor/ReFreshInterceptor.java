package org.example.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ReFreshInterceptor implements HandlerInterceptor {

    // 建议与 JwtUtils 共用同一个密钥来源，避免不一致
// JwtUtils 和 ReFreshInterceptor 两处
    public static final long EXPIRE_TIME = 12 * 60 * 60 * 1000L;
    public static final long RENEW_THRESHOLD = 10 * 60 * 1000L;      // 剩 10 分钟时续

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String token = request.getHeader("token");

        // 没带 token，不属于本拦截器职责，放行交给后续
        if (token == null || token.isEmpty()) {
            return true;
        }

        try {
            Claims claims = JwtUtils.parseToken(token);
            long remaining = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (remaining <= RENEW_THRESHOLD) {
                Map<String, Object> newClaims = new HashMap<>();
                newClaims.put("id", claims.get("id"));
                newClaims.put("username", claims.get("username"));

                String newToken = Jwts.builder()
                        .signWith(SignatureAlgorithm.HS256, JwtUtils.SECRET_KEY) // 同一密钥
                        .addClaims(newClaims)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                        .compact();

                response.setHeader("newToken", newToken);
                log.info("Token 已续签，剩余有效期 {}ms", remaining);
            }

            // 把解析结果传给后续拦截器，避免重复解析
            request.setAttribute("claims", claims);
        }catch (JwtException | IllegalArgumentException e){
            log.debug("Token 已过期或格式错误");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}