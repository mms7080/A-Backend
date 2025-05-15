package com.example.demo.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.OAuthLogin.userDetailsServiceCustom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{
    
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    CookieUtil cookieUtil;
    @Autowired
    userDetailsServiceCustom detailsService;

    @Value("${spring.security.jwt.cookie.name}")
    String jwtCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHead="";
        if(request.getCookies()!=null){/* 요청에 쿠키가 있다면, 설정된 이름(jwtCookieName)의 쿠키를 찾아 JWT 값을 추출 */
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals(jwtCookieName)){
                    authHead=cookie.getValue();
                    break;
                }
            }
        }
        
        String username="";
        if(StringUtils.hasText(authHead)){/* JWT가 비어있지 않다면 */
            if(jwtUtil.usefulToken(authHead))/* 토큰이 유효하고 만료되지 않았는지 검사 */
                username=jwtUtil.extractUsername(authHead);
            else
                cookieUtil.RemoveJWTCookie(response);/* 유효하지 않으면 쿠키 제거 */
        }

        if(StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication()==null){/* 현재 인증된 사용자 객체가 이미 있는지 검사 */
            UserDetails userDetails=detailsService.loadUserByUsername(username);/* DB에서 사용자 정보 불러옴 */
            UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(upat);/* 인증된 사용자 객체 등록 */
        }

        filterChain.doFilter(request,response);/* 필터 체인의 다음 필터로 요청과 응답을 넘김 */
    }

}
