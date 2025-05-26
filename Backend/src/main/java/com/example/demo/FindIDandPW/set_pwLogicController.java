package com.example.demo.FindIDandPW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

@Controller
public class set_pwLogicController {

    @Autowired
    DAOUser daoUser;

    @Value("${spring.security.cors.site}")
    String corsOrigin;

    @PostMapping("/set_pw/logic")
    public String set_pw_logic(/* 비밀번호 재설정 로직 작성 */
        @RequestParam("id") String username,
        @RequestParam("pw") String password
    ){
        User user = daoUser.findUsername(username);/* 비밀번호를 변경할 유저 정보 */

        user.setPassword(new BCryptPasswordEncoder().encode(password));
        daoUser.Modify(user);
        return "redirect:"+corsOrigin+"/signin";
    }

}
