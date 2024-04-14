package com.mizore.easybuy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Configuration
@ConfigurationProperties("jwt")
public class JWTUtil {

    private static String secretKeyStr = "huHAQ21P5fsGgpnHKipREwz4zgwUxJ8bGl+gmW4mbl4=";

    private static int ttl = 99;  // 单位： 天

    private static SecretKey secretKey;

    public static String generateJWT(Map<String, Object> claims) {
        if (secretKey == null) {
            secretKey = getSecretKey(secretKeyStr);
        }

        long msTtl =  24L * 3600 * 1000 * ttl;

        return  Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + msTtl))
                .signWith(secretKey)
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        if (secretKey == null) {
            secretKey = getSecretKey(secretKeyStr);
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }


    public static SecretKey getSecretKey(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

}
