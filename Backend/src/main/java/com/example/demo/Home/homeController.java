package com.example.demo.Home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.annotations.Auth;
import com.example.demo.User.User;

@Controller
public class homeController{

    @GetMapping("/home")
    public String home(@Auth User user,Model model){
        model.addAttribute("isLogin",user!=null);/* 현재 접속 중인 user 변수가 null인지 아닌지에 따라 isLogin 설정 */
        if(user!=null){
            model.addAttribute("user",user);/* 유저의 이름 또는 닉네임을 홈 화면에 표시하기 위해 값 설정  */
            if(user.getPlatform()!= null)model.addAttribute("oauth2",true);/* OAuth2로 로그인한 사용자인지 판별해서 마이페이지 들어가지 못하게 하기 위해 설정 */
            else model.addAttribute("oauth2",false);
        }
        return "home";
    }

}