package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.User;
import com.example.demo.dao.DAOUser;

@RestController
public class joinLogicController{

    @Autowired
    DAOUser daoUser;

    @PostMapping("/join/logic")
    public Map<String, Object> join_logic(/* 회원가입 로직 작성 */
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
        ){
        
        Map<String, Object> response = new HashMap<>();

        if(daoUser.usernameExists(username)){/* 중복되는 ID가 존재하면 다시 회원가입 홈페이지로 돌려보내고, idexists=true 대입 */
            response.put("success", false);/* 로그인 실패 */
            response.put("reason", "idexists");/* 아이디가 이미 존재한다는 이유를 포함 */
            return response;
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
            response.put("success", true);/* 로그인 성공 */
            return response;
        }            
    }

}