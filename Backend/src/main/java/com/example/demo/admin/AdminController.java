package com.example.demo.admin;

import com.example.demo.User.UserRepository;
import com.example.demo.Store.StoreRepository;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/admin") 
public class AdminController {


    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public AdminController(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    // 총 회원 수
    @GetMapping("/user-count")
    public long getUserCount() {
        return userRepository.count();
    }

    // 유저정보
    @GetMapping("/users")
    public List<?> getAllUsers() {
        return userRepository.findAll();
}


    // 총 스토어 상품 수
    @GetMapping("/store-count")
    public long getStoreCount() {
        return storeRepository.count();
    }
    // 스토어 목록
    @GetMapping("/store")
    public List<?> getAllstores() {
        return storeRepository.findAll();
}

}
