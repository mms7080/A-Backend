package com.example.demo.Test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testController {

    @GetMapping("/secure")
    @ResponseBody
    public String A(){/* 로그인 상태여야 접근 가능한 페이지 */
        return "SECURE!";
    }
    
    @GetMapping("/public")
    @ResponseBody
    public String B(){/* 로그인 하지 않아도 접근 가능한 페이지 */
        return "PUBLIC!";
    }

    @GetMapping("/check")
    @ResponseBody
    public String C(){/* ADMIN 권한이 필요한 페이지 */
        return "CHECK!";
    }

}
