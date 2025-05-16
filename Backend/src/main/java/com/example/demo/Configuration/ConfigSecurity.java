package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.JWT.CookieUtil;
import com.example.demo.JWT.JwtAuthorizationFilter;
import com.example.demo.JWT.JwtUtil;
import com.example.demo.OAuthLogin.userDetailsServiceCustom;
import com.example.demo.User.UserInfo;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class ConfigSecurity{/* 로그인 로직 */

    @Autowired
    userDetailsServiceCustom udsc;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    CookieUtil cookieUtil;
    @Autowired
    JwtAuthorizationFilter jwtFilter;

    @Value("${spring.security.cors.site}")
    String corsOrigin;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource cors(){
        CorsConfiguration config=new CorsConfiguration();
        config.addAllowedOrigin(corsOrigin);/* 허용할 Origin(프론트 주소) */
        config.addAllowedMethod("*");/* 모든 HTTP 메서드 허용 */
        config.addAllowedHeader("*");/* 모든 헤더 허용 */
        config.setAllowCredentials(true);/* 쿠키 포함 요청 허용 */

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);/* 모든 경로에 대해 적용 */
        return source;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception{
        return http
        .csrf(csrf->csrf.disable())/* CSRF 비활성화 */
        /* .ignoringRequestMatchers("/public","/private","/signin/logic","/logout","/join/logic","/modify/logic","/find_id/logic","/find_pw/logic","/set_pw/logic"))// JWT 토큰을 사용하면서 더 이상 사용하지 않게 된 부분 */
        .authorizeHttpRequests(auth->auth.dispatcherTypeMatchers(DispatcherType.FORWARD)/* JSP forward는 허용 */
        .permitAll()
        .requestMatchers("/secure","/logout")/* 로그인 필요 */
        .authenticated()
        .requestMatchers("/check")/* ADMIN 권한 필요 */
        .hasAnyRole("ADMIN")
        .anyRequest()/* 나머지 허용 */
        .permitAll()
        )
        .formLogin(login->login
        .loginPage(corsOrigin+"/signin")/* 로그인 페이지 */
        .loginProcessingUrl("/signin/logic")/* 로그인 처리 URL */
        .failureUrl(corsOrigin+"/signin")/* 실패시 리다이렉트 */
        .usernameParameter("id")/* 입력 폼의 id 필드명 */
        .passwordParameter("pw")/* 입력 폼의 pw 필드명 */
        .successHandler((req,res,auth)->{
            String token=jwtUtil.generateToken((UserInfo)auth.getPrincipal());
            cookieUtil.GenerateJWTCookie(token,res);/* 쿠키로 JWT 발급 */
            res.sendRedirect(corsOrigin+"/home");/* 로그인 성공 시 홈 이동 */
        })
        )
        .logout(logout->logout
        .clearAuthentication(true)
        .deleteCookies("JSESSIONID")/* 세션 쿠키 삭제 */
        .invalidateHttpSession(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
        .logoutSuccessHandler((req,res,auth)->{
            cookieUtil.RemoveJWTCookie(res);/* JWT 쿠키 제거 */
            res.sendRedirect(corsOrigin+"/home");/* 로그아웃 후 홈으로 */
        })
        )
        .rememberMe(rm->rm
        .key("Z7EMefBrld4tlbRJgxvJKVLcUnTt2PbIHawfCUgzmO4Y8O9uvavu9SeFZvoStUrZtqoBxSt9Y7yWLCX1D82sHYROIBIjeQy5WGx4fY2i1IUbPXs43p8UzfEiSRG2DSvE")/* 내부적으로 토큰 서명에 사용 */
        .tokenValiditySeconds(3600*24*7)/* 7일 유지 */
        .rememberMeCookieDomain("domain")
        .rememberMeCookieName("name")
        .useSecureCookie(false)
        .alwaysRemember(false)
        .rememberMeParameter("remember-login")/* 폼 필드 이름 */
        )
        .oauth2Login(login->login
        .loginPage(corsOrigin+"/signin")
        .failureUrl(corsOrigin+"/signin")
        .userInfoEndpoint(info->info.userService(udsc))/* 사용자 정보 처리 */
        .successHandler((req,res,auth)->{
            String token=jwtUtil.generateToken((UserInfo)auth.getPrincipal());
            cookieUtil.GenerateJWTCookie(token,res);
            res.sendRedirect(corsOrigin+"/home");
        })
        )
        .exceptionHandling(err->err
        .authenticationEntryPoint((req,res,auth)->{/* 인증 실패 처리 */
            res.getWriter().write("{\"message\":\"Authentication Error\"}");
        })
        )
        .sessionManagement(session->session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)/* JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가 */
        .userDetailsService(udsc)
        .getOrBuild();
    }

}