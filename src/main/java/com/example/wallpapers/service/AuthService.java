package com.example.wallpapers.service;

import com.example.wallpapers.enums.AuthStatus;
import com.example.wallpapers.jwt.JwtAuthentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class AuthService {
    private final SecretKey jwtSecret;

    public AuthService(@Value("${jwt.secret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    public AuthStatus validateToken(@NonNull String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return AuthStatus.TOKEN_VALID;
        } catch (ExpiredJwtException expEx) {
//            log.error("Token expired", expEx);
            return AuthStatus.TOKEN_EXPIRED;
        } catch (UnsupportedJwtException unsEx) {
//            log.error("Unsupported jwt", unsEx);
            return AuthStatus.TOKEN_UNSUPPORTED;
        } catch (MalformedJwtException mjEx) {
//            log.error("Malformed jwt", mjEx);
            return AuthStatus.TOKEN_MALFORMED;
        } catch (SignatureException sEx) {
//            log.error("Invalid signature", sEx);
            return AuthStatus.TOKEN_INVALID_SIGNATURE;
        } catch (Exception e) {
//            log.error("invalid token", e);
            return AuthStatus.TOKEN_INVALID;
        }
    }
    public Claims getClaims(@NonNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtAuthentication getAuthentication() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
