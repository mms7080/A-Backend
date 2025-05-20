package com.example.demo.Modify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Annotations.Auth;
import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

@Controller
public class modifyLogicController{

    @Autowired
    DAOUser daoUser;

    @PostMapping("/modify/logic")
    public String modify_logic(
        @RequestParam("id") String username,
        @RequestParam("pw") String password,
        @RequestParam("name") String name,
        @RequestParam("phone_number") String phone,
        @RequestParam("email") String email,
        @RequestParam("birthdate") String birthdate,
        @RequestParam(name="gender",required=false) String gender,
        @RequestParam("zipcode") String zipcode,
        @RequestParam("address") String address,
        @RequestParam("address_detail") String address_detail,
        @Auth User user){

        
        /* 마이페이지에서 수정할 수 있는 정보들인 비밀번호, address_detail, phone, email, birthdate, gender 수정사항 반영 */
        if(!password.isEmpty())user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setAddress_detail(address_detail);
        user.setPhone(phone);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setGender(gender);
        daoUser.Modify(user);/* 수정사항을 Modify 함수로 적용 */

        return "redirect:http://localhost:3000/mypage";
    }
    
}