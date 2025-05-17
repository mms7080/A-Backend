package com.example.demo.Configuration;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 상영시간 아시아/ 서울 시간으로 설정
@Configuration
public class Appconfig {
    @Bean
    public Clock clock(){
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
