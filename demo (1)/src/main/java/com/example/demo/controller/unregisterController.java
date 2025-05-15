package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.annotations.Auth;
import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;
import com.example.demo.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class unregisterController {

    @Autowired
    CookieUtil cookieUtil;
    @Autowired
    DAOUser daoUser;

    @GetMapping("/unregister")
    public String unregister(HttpServletResponse response,@Auth User user,Model model){
        daoUser.Delete(user);/* 현재 접속중인 회원의 탈퇴 */
        cookieUtil.RemoveJWTCookie(response);/* JWT 쿠키 제거 */
        model.addAttribute("isLogin",false);/* 회원탈퇴 직후에는 로그인 상태가 아니라는 것을 표현하기 위해 isLogin에 false 대입 */
        return "home";/* 홈 화면으로 이동 */
    }    

}
