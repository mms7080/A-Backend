package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class find_pwLogicController {
    
    @Autowired
    DAOUser daoUser;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/find_pw/logic")
    public String find_pw_logic(/* 비밀번호 찾기 로직 작성 */
        @RequestParam("id") String username,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone_number") String phone,
        @RequestParam("method") String method,
        HttpServletResponse response,
        Model model
    ){
        User user=null;

        if(method.equals("email"))/* 이메일을 활용한 찾기 */
            user=daoUser.findUsernameNameEmail(username,name,email);
        else if(method.equals("phone_number"))/* 휴대폰 번호를 활용한 찾기 */
            user=daoUser.findUsernameNamePhone(username,name,phone);

        if(user==null){
            model.addAttribute("foundPW",false);
            model.addAttribute("noInfo",true);/* 해당하는 정보가 존재하지 않는다는 텍스트를 띄우기 위한 변수 설정 */
        }
        else{
            model.addAttribute("foundPW",true);
            
            String resetToken = jwtUtil.generateResetToken(user.getUsername());/* Reset용 JWT 발급 */

            Cookie cookie = new Cookie("reset_token", resetToken);/* 쿠키로 전달 (or 쿼리 파라미터도 가능) */
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(5 * 60); /* 만료기한은 5분으로 짧게 하는 것이 보안상 좋음 */
            response.addCookie(cookie);
        }

        return "find_pw";
    }

}
