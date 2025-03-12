package com.example.be.user.jwt;

import com.example.be.user.exception.AuthException;
import com.example.be.user.exception.UserErrorCode;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expiration.access}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.expiration.refresh}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private SecretKey secretKey;

    @PostConstruct
    protected void initSecretKey() {
//        this.secretKey = new SecretKeySpec(secret.getBytes(UTF_8),
//                Jwts.SIG.HS512.key().build().getAlgorithm());
        this.secretKey = Jwts.SIG.HS512.key().build();
    }

    public String generateAccessToken(Authentication authentication, String userId) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME, userId, "access");
    }

    public String generateRefreshToken(Authentication authentication, String userId) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, userId, "refresh");
    }

    private String generateToken(Authentication authentication, Long expirationMs, String userId, String category) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        return Jwts.builder()
                .subject(userId)
                .claim("category", category)
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid token signature", e);
            throw new AuthException(UserErrorCode.INVALID_TOKEN_SIGNATURE);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid token format", e);
            throw new AuthException(UserErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired token", e);
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("parseClaims Error: {}", e.getMessage());
            throw new AuthException(UserErrorCode.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("authorities").toString());

        User userPrincipal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(userPrincipal, token, authorities);
    }
}
