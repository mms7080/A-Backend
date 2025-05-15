package com.example.demo.JWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
    
    @Value("${spring.security.jwt.expires}")
    Integer jwtExpire;/* 쿠키의 생성시점으로부터 만료시점까지 걸리는 초 단위 시간(예 : 3600) */
    @Value("${spring.security.jwt.cookie.name}")
    String jwtAuthCookieName;/* 쿠키의 이름 */
    @Value("${spring.security.cors.same.domain}")
    String corsDomain;/* 쿠키가 유효한 도메인 및 서브도메인을 지정 */

    public void GenerateJWTCookie(String jwt,HttpServletResponse response){
        Cookie cookie=new Cookie(jwtAuthCookieName,jwt);/* 쿠키 생성 */
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpire);
        cookie.setDomain(corsDomain);
        /* cookie.setSecure(true); // https만 이용 가능한 쿠키로 설정 - 개발시에는 false, 배포시에는 true 지정 */
        cookie.setAttribute("SameSite","Lax");
        response.addCookie(cookie);
    }

    public void RemoveJWTCookie(HttpServletResponse response){
        Cookie cookie=new Cookie(jwtAuthCookieName,"");/* 생성할 때와 완전히 똑같은 속성을 적용하게 해야 삭제됨 */
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);/* 만료기한을 0초 뒤로 설정하여 삭제 */
        cookie.setDomain(corsDomain);
        /* cookie.setSecure(true); // https만 이용 가능한 쿠키로 설정 - 개발시에는 false, 배포시에는 true 지정 */
        cookie.setAttribute("SameSite","Lax");
        response.addCookie(cookie);
    }

}
