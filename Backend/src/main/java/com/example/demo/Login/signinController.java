package com.example.demo.Login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class signinController{

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

}