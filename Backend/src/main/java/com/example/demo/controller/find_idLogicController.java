package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;

@Controller
public class find_idLogicController {
    
    @Autowired
    DAOUser daoUser;

    @PostMapping("/find_id/logic")
    public String find_id_logic(/* ID 찾기 로직 작성 */
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone_number") String phone,
        @RequestParam("method") String method,
        Model model
    ){
        User user=null;

        if(method.equals("email"))/* 이메일을 활용한 찾기 */
            user=daoUser.findNameEmail(name, email);
        else if(method.equals("phone_number"))/* 휴대폰 번호를 활용한 찾기 */
            user=daoUser.findNamePhone(name, phone);

        if(user==null){/* 해당하는 정보가 DB에 없을 경우 */
            model.addAttribute("foundID",false);
            model.addAttribute("noInfo",true);/* 해당하는 정보가 존재하지 않는다는 텍스트를 띄우기 위한 변수 설정 */
        }
        else{
            model.addAttribute("foundID",true);
            model.addAttribute("resultID",user.getUsername().substring(0,user.getUsername().length()-3)+"***");/* 아이디의 뒷 세 자리 부분만 빼고 표현 */
        }

        return "find_id";
    }

}
