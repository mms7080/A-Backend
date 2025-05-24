package com.example.demo.Modify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.cors.site}")
    String corsOrigin;

    @PostMapping("/modify/logic")
    public String modify_logic(
        @RequestParam("pw") String password,
        @RequestParam("phone_number") String phone,
        @RequestParam("email") String email,
        @RequestParam("birthdate") String birthdate,
        @RequestParam(name="gender",required=false) String gender,
        @RequestParam("zipcode") String zipcode,
        @RequestParam("address") String address,
        @RequestParam("address_detail") String address_detail,
        @Auth User user){

        
        /* 마이페이지에서 수정할 수 있는 정보들인 비밀번호, phone, email, birthdate, gender, zipcode, address, address_detail 수정사항 반영 */
        if(!password.isEmpty())user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setPhone(phone);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setGender(gender);
        user.setZipcode(zipcode);
        user.setAddress(address);
        user.setAddress_detail(address_detail);
        daoUser.Modify(user);/* 수정사항을 Modify 함수로 적용 */

        return "redirect:"+corsOrigin+"/mypage";
    }
    
}