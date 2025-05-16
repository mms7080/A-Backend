package com.example.demo.Modify;

import com.example.demo.annotations.Auth;
import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        @Auth User user,
        Model model){

        if(user!=null){/* 현재 접속 중인 유저가 있을 경우 */
            /* 마이페이지에서 수정할 수 있는 정보들인 비밀번호, address_detail, phone, email, birthdate, gender 수정사항 반영 */
            if(!password.isEmpty())user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setAddress_detail(address_detail);
            user.setPhone(phone);
            user.setEmail(email);
            user.setBirthdate(birthdate);
            user.setGender(gender);
            daoUser.Modify(user);/* 수정사항을 Modify 함수로 적용 */
        }
        else{/* 현재 접속 중인 유저가 없을 경우 */
            user=new User(/* 새로운 유저 정보를 취합해서 새로이 DB에 추가 */
                null,
                username,
                new BCryptPasswordEncoder().encode(password),
                name,
                phone,
                email,
                birthdate,
                gender,
                zipcode,
                address,
                address_detail,
                List.of("USER"),
                null
            );
            daoUser.Insert(user);/* DB에 Insert로 삽입 */
        }      

        model.addAttribute("isLogin",true);/* 마이페이지에서 수정하면 로그인 상태가 되었다는 의미이므로 isLogin에 true 대입 */
        model.addAttribute("user",user);/* 홈 화면에서 유저 정보 표시 위해 user에 정보 대입 */
        return "home";
    }
    
}