package com.example.demo.Join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")/* 프론트주소 바뀌면 같이 바꾸어 줘어야 함. */
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
        @RequestParam("address_detail") String address_detail
        ){

        /* ID 중복여부는 프론트엔드에서 확인하므로 즉시 DB에 회원정보 입력 */
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
                "USER",
                null
            )
        );

        return "redirect:http://localhost:3000/signin";
    }

}