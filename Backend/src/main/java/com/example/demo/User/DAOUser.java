package com.example.demo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Component
public class DAOUser {

    @Autowired
    private UserRepository userRepository;

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        return users.isEmpty() ? null : users.get(0);
    }

    public User findUsernameNameEmail(String username, String name, String email) {
        return userRepository.findByUsernameAndNameAndEmail(username, name, email).orElse(null);
    }

    public User findUsernameNamePhone(String username, String name, String phone) {
        return userRepository.findByUsernameAndNameAndPhone(username, name, phone).orElse(null);
    }

    public User findNameEmail(String name, String email) {
        return userRepository.findByNameAndEmail(name, email).orElse(null);
    }

    public User findNamePhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone).orElse(null);
    }

    public void Insert(User user) {
        user.setId(null);
        userRepository.save(user);
    }

    public void Modify(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void Delete(User user) {
        userRepository.deleteById(user.getId());
    }

    // 초기 데이터 입력
    @PostConstruct
    @Transactional
    public void testUsers() {
        if(userRepository.count() > 0) return;
        Insert(/* 테스트를 위한 유저 정보 입력 1 */
            new User(
                null,
                "root",
                new BCryptPasswordEncoder().encode("4321"),
                "관리자",
                "01011223344",
                "honggildong@naver.com",
                "1990-10-31",
                "남성",
                "21176",
                "서울시 은평구",
                "문새로72번길 13",
                "ADMIN",
                null,
                "2025-05-16",
                null
            )
        );
        Insert(/* 테스트를 위한 유저 정보 입력 2 */
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
                "2025-05-23",
                null
            )
        );        

        Insert(/* 테스트를 위한 유저 정보 입력 3 */
            new User(
                null,
                "bum",
                new BCryptPasswordEncoder().encode("1234"),
                "박범수",
                "01030600342",
                "mms7080@naver.com",
                "1990-10-31",
                "남성",
                "21176",
                "서울시 은평구",
                "문새로72번길 13",
                "ADMIN",
                null,
                "2025-05-16",
                null
            )
        ); 
    }
}
