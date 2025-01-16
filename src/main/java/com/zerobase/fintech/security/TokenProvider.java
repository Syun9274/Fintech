package com.zerobase.fintech.security;

import com.zerobase.fintech.common.enums.UserRole;
import com.zerobase.fintech.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final UserService userService;

    private static final String KEY_ROLE = "role";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 3; // 3 hours

    private Key key;

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        String secretKey = dotenv.get("JWT_SECRET");

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET is not defined in the environment variables.");
        }

        if (secretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET must be at least 32 bytes long.");
        }

        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성.
     *
     * @param email 사용자 이름.
     * @param role 사용자 권한 리스트.
     * @return 생성된 JWT 토큰 문자열.
     */
    public String generateToken(String email, UserRole role) {

        // 사용자 정보 추가
        var claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, role);

        // 토큰 만료 시간 설정
        var now = new Date();
        var expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 토큰 생성
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")           // 토큰 타입
                .setClaims(claims)                          // 사용자 정보
                .setIssuedAt(now)                           // 발행 시간
                .setExpiration(expirationDate)              // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)    // 서명 방식
                .compact();                                 // 토큰 생성
    }

    /**
     * JWT 토큰으로부터 인증 객체 생성.
     *
     * @param token JWT 토큰.
     * @return 인증(Authentication) 객체.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getUsername(token));
        List<GrantedAuthority> authorities = getAuthorities(token);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    /**
     * 토큰에서 사용자 이름(식별자) 추출.
     *
     * @param token JWT 토큰.
     * @return 사용자 이름(식별자).
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * JWT 토큰에서 권한 정보 추출.
     *
     * @param token JWT 토큰.
     * @return 권한 리스트.
     */
    public List<GrantedAuthority> getAuthorities(String token) {

        // Claims에서 roles 추출
        Claims claims = parseClaims(token);
        Object rolesObject = claims.get(KEY_ROLE);

        List<String> roles;

        if (rolesObject instanceof List) {
            // rolesObject가 List인 경우
            roles = ((List<?>) rolesObject).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .toList();
        } else if (rolesObject instanceof String) {
            // rolesObject가 단일 문자열인 경우
            roles = List.of((String) rolesObject);
        } else {
            roles = new ArrayList<>();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * JWT 토큰에서 Claims 추출.
     *
     * @param token JWT 토큰.
     * @return Claims 객체.
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)     // 서명 키 설정
                    .build()
                    .parseClaimsJws(token)  // JWT 파싱 및 유효성 검증
                    .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();

        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    /**
     * JWT 토큰 유효성 검증.
     *
     * @param token JWT 토큰.
     * @return 유효한 토큰이면 true, 그렇지 않으면 false.
     */
    public boolean validateToken(String token) {
        try {
            // Claims를 파싱하여 만료 시간 확인
            var claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
