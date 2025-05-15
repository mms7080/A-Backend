package com.example.demo.JWT;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.User.UserInfo;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    String jwtSecret;
    @Value("${spring.security.jwt.expires}")
    Integer jwtExpire;
    SecretKey key;

    @PostConstruct
    public void init(){
        this.key=Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));/* JWT를 생성하거나 검증하는 데 사용할 비밀 키 설정 */
    }

    public String generateToken(UserInfo userInfo){
        Map<String,Object> claims=new HashMap<>();/* JWT 토큰에 포함될 사용자 정보를 저장하는 Map 객체 */
        claims.put("username",userInfo.getUsername());/* 사용자 이름을 Map 객체에 포함 */
        return createToken(claims,userInfo.getUsername());/* 본격적인 토큰 생성 */
    }

    public String generateResetToken(String username){
        Map<String,Object> claims=new HashMap<>();
        claims.put("type","reset");/* 토큰 타입 명시 (비밀번호 재설정용) */
        claims.put("username",username);/* 사용자 이름 */

        return Jwts.builder()
            .claims(claims)
            .subject("reset-password")/* subject도 목적을 명시 */
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(5,ChronoUnit.MINUTES)))/* 5분 유효 */
            .signWith(key)
            .compact();
    }

    public String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder()
        .claims(claims)/* 사용자 ID 등 내부 정보(페이로드에 포함) */
        .subject(subject)/* JWT의 주체(이 경우는 ID) */
        .issuedAt(Date.from(Instant.now()))/* 생성 시간 */
        .expiration(Date.from(Instant.now().plus(jwtExpire,ChronoUnit.SECONDS)))/* 만료 기한 */
        .signWith(key)/* 암호화 방식 */
        .compact();/* 최종 문자열 생성 */
    }

    public Claims extractClaims(String token){
        return Jwts.parser()/* 빌더 객체 생성 */
        .verifyWith(key)/* 서명 검증용 키 지정 */
        .build()/* 파서 인스턴스 생성 */
        .parseSignedClaims(token)/* 클레임 정보 추출 */
        .getPayload();/* 페이로드만 가져옴 */
    }

    public String extractUsername(String token){/* 토큰에서 subject(여기서는 사용자 ID)를 추출 */
        return (String)extractClaims(token).getSubject();
    }

    public Boolean validateToken(String token){/* 토큰의 형식과 서명이 유효한지 확인 */
        try{extractClaims(token); return true;}
        catch(Exception e){return false;}
    }

    public Boolean tokenExpired(String token){/* 토큰이 현재 시간 기준으로 만료됐는지 확인 */
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Boolean usefulToken(String token){/* 유효하고 만료되지 않은 토큰인지 확인 */
        return validateToken(token) && !tokenExpired(token);
    }

}
