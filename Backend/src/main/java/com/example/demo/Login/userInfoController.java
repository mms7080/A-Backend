package com.example.demo.Login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Annotations.Auth;
import com.example.demo.User.User;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")/* 프론트주소 바뀌면 같이 바꾸어 줘어야 함. */
public class userInfoController {

    @GetMapping("/userinfo")
    public User getUserInfo(@Auth User user) {/* 현재 로그인 한 사용자 정보를 리턴 */
        return user;
    }

}
