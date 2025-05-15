package com.example.demo.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.User.DAOUser;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")/* 프론트주소 바뀌면 같이 바꾸어 줘어야 함. */
public class idExistController{

    @Autowired
    DAOUser daoUser;

    @GetMapping("/idexist")
    public Boolean join(@RequestParam("id") String username){/* 유저 아이디 중복 검사를 위한 페이지 */
        return daoUser.usernameExists(username);/* 중복된 아이디가 존재하면 true 리턴, 중복된 아이디가 없으면 false 리턴 */
    }
    
}