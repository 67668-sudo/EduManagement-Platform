package org.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    public static void main(String[] args) {
        JwtTest jwtTest = new JwtTest();
        jwtTest.testGenerateToken();
    }

    public void testGenerateToken() {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", "admin");
        dataMap.put("id", "1");
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, "VGxjNWNGVnFWbmRVTURReFZFUktiazVYUm1oVFVUMDk=")
                .addClaims(dataMap)
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000))
                .compact();
        System.out.println(token);
    }
}
