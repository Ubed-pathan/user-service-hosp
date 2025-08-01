package com.appointment.user_service.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${JWT_PRIVATE_KEY}")
    private String base64PrivateKey;

    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {
        if (base64PrivateKey == null || base64PrivateKey.isEmpty()) {
            throw new IllegalStateException("PRIVATE_KEY environment variable is not set");
        }

        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = keyFactory.generatePrivate(keySpec);
    }

    public String generateToken(String userName, String userId, String email, String role) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer("user-service")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600 * 24 * 30))) // 30 days
                .addClaims(Map.of(
                        "userName", userName,
                        "email", email,
                        "role", role
                ))
                .setHeaderParam("kid", "user-service-key") // Key ID for API Gateway
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
