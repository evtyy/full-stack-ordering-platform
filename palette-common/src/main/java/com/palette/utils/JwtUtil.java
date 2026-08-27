package com.palette.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * Generate a jwt
     * Uses the Hs256 algorithm, with a fixed secret key
     *
     * @param secretKey jwt secret key
     * @param ttlMillis jwt expiration time (milliseconds)
     * @param claims    information to set
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // Specify the signature algorithm used when signing, i.e. the header part
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Time at which the JWT is generated
        long expMillis = System.currentTimeMillis() + ttlMillis * 1000000;
        Date exp = new Date(expMillis);

        // Set the jwt body
        JwtBuilder builder = Jwts.builder()
                // If there are private claims, they must be set before the standard claims, since this assigns claims to the builder and setting them after the standard claims would overwrite them
                .setClaims(claims)
                // Set the signature algorithm and secret key used for signing
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the expiration time
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token decryption
     *
     * @param secretKey jwt secret key. This key must be kept safe on the server side and never exposed, otherwise the signature can be forged. If integrating with multiple clients, consider using multiple keys
     * @param token     the encrypted token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // Get the DefaultJwtParser
        Claims claims = Jwts.parser()
                // Set the signing key
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the jwt to be parsed
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
