package com.example.demo.controller;

import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class joinLogicController{

    @Autowired
    DAOUser daoUser;

    @PostMapping("/join/logic")
    public String join_logic(/* 회원가입 로직 작성 */
        @RequestParam("id") String username,
        @RequestParam("pw") String password,
        @RequestParam("name") String name,
        @RequestParam("area_code") String area_code,
        @RequestParam("phone_first") String phone_first,
        @RequestParam("phone_second") String phone_second,
        @RequestParam("email_id") String email_id,
        @RequestParam("email_address") String email_address,
        @RequestParam("birthdate") String birthdate,
        @RequestParam(name="gender",required=false) String gender,
        @RequestParam("zipcode") String zipcode,
        @RequestParam("address") String address,
        @RequestParam("address_detail") String address_detail,
        Model model){

        if(daoUser.usernameExists(username)){/* 중복되는 ID가 존재하면 다시 회원가입 홈페이지로 돌려보내고, idexists=true 대입 */
            model.addAttribute("idexists",true);
            return "join";/* 회원가입 페이지로 돌아감 */
        }
        else{/* 중복되는 ID가 존재하지 않으면 DB에 회원정보 입력 */
            daoUser.Insert(
                new User(
                    null,
                    username,
                    new BCryptPasswordEncoder().encode(password),/* 비밀번호 암호화 */
                    name,
                    area_code+phone_first+phone_second,
                    (!email_id.isEmpty())?(email_id+'@'+email_address):"",
                    birthdate,
                    gender,
                    zipcode,
                    address,
                    address_detail,
                    List.of("USER"),
                    null
                )
            );
        }            

        return "signin";/* 회원가입 성공시 로그인 페이지로 자동 이동 */
    }

}