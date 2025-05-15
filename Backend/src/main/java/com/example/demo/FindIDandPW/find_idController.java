package com.example.demo.FindIDandPW;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class find_idController {
    
     @GetMapping("/find_id")
    public String find_id(Model model){
        model.addAttribute("foundID",false);/* 처음에는 아이디를 아직 못 찾았으니 false로 지정 */
        return "find_id";
    }

}
