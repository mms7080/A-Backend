package com.example.demo.Unregister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Annotations.Auth;
import com.example.demo.JWT.CookieUtil;
import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class unregisterController {

    @Autowired
    CookieUtil cookieUtil;
    @Autowired
    DAOUser daoUser;
    @Value("${spring.security.cors.site}")
    String corsOrigin;

    @GetMapping("/unregister")
    public String unregister(HttpServletResponse response,@Auth User user){
        daoUser.Delete(user);/* 현재 접속중인 회원의 탈퇴 */
        cookieUtil.RemoveJWTCookie(response);/* JWT 쿠키 제거 */
        return "redirect:"+corsOrigin+"/home";/* 홈 화면으로 이동 */
    }    

}
