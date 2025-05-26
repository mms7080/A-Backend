package com.example.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner{

    @Autowired
    DAOUser daoUser;

    @Override
    public void run(String...args) throws Exception{
        daoUser.Insert(/* 테스트를 위한 유저 정보 입력 1 */
            new User(
                null,
                "root",
                new BCryptPasswordEncoder().encode("1234"),
                "홍길동",
                "01011223344",
                "honggildong@naver.com",
                "1990-10-31",
                "남성",
                "21176",
                "서울시 은평구",
                "문새로72번길 13",
                "ADMIN",
                null,
                "2025-05-16"
            )
        );
        daoUser.Insert(/* 테스트를 위한 유저 정보 입력 2 */
            new User(
                null,
                "shin1234",
                new BCryptPasswordEncoder().encode("1234"),
                "신사임당",
                "01099887766",
                "shinsayimdang@yahoo.com",
                "1998-07-05",
                "여성",
                "36569",
                "경기도 남양주시",
                "조화로14번길 26",
                "USER",
                null,
                "2025-05-23"
            )
        );        
    }    

}
