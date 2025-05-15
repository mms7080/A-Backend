package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class set_pwLogicController {

    @Autowired
    DAOUser daoUser;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/set_pw/logic")
    public String set_pw_logic(/* 비밀번호 재설정 로직 작성 */
        HttpServletRequest request,
        @RequestParam("pw") String password
    ){

         String resetToken = null;

        if (request.getCookies() != null) {/* 쿠키에서 토큰 꺼내기 */
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("reset_token")) {
                    resetToken = cookie.getValue();
                    break;
                }
            }
        }

        if (resetToken == null || !jwtUtil.usefulToken(resetToken))
            return "find_pw";/* 만료되었거나 잘못된 토큰이 들어오면 비밀번호 찾기 페이지로 다시 재이동 */

        String username = jwtUtil.extractClaims(resetToken).get("username", String.class);
        User user = daoUser.findUsername(username);/* 비밀번호를 변경할 유저 정보 */

        user.setPassword(new BCryptPasswordEncoder().encode(password));
        daoUser.Modify(user);
        return "signin";
    }
}
