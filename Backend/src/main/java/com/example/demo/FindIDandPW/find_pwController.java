package com.example.demo.FindIDandPW;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class find_pwController {
    
     @GetMapping("/find_pw")
    public String find_pw(Model model){
        model.addAttribute("foundPW",false);
        return "find_pw";
    }

}
