package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.annotations.Auth;
import com.example.demo.classes.User;

@Controller
public class modifyController {

    @GetMapping("/modify")
    public String modify(@Auth User user,Model model){
        if(user!=null)model.addAttribute("user",user);/* 현재 접속 중인 유저가 존재하면, 유저 정보를 마이페이지에서 표기하기 위해 user 변수활용 */
        return "modify";
    }

}
