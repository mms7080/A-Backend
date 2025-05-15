package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.classes.User;
import com.example.demo.repository.UserRepository;

@Component
public class DAOUser {
    @Autowired
    private UserRepository userRepository;

    public boolean usernameExists(String username){/* 유저의 ID가 존재하는 지 여부를 확인하는 메서드(중복확인) */
        return userRepository.existsByUsername(username);
    }

    public User findUsername(String username){/* 유저의 ID를 기반으로 유저를 찾아내서, 해당 유저 정보를 리턴 */
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findUsernameNameEmail(String username,String name,String email){/* 유저 ID, 이름, 이메일 기반으로 찾는 함수 */
        return userRepository.findByUsernameAndNameAndEmail(username,name,email).orElse(null);
    }

    public User findUsernameNamePhone(String username,String name,String phone){/* 유저 ID, 이름, 전화번호 기반으로 찾는 함수 */
        return userRepository.findByUsernameAndNameAndPhone(username,name,phone).orElse(null);
    }

    public User findNameEmail(String name,String email){/* 유저 이름, 이메일 기반으로 찾는 함수 */
        return userRepository.findByNameAndEmail(name,email).orElse(null);
    }
    
    public User findNamePhone(String name,String phone){/* 유저 이름, 전화번호 기반으로 찾는 함수 */
        return userRepository.findByNameAndPhone(name,phone).orElse(null);
    }

    public void Insert(User user){/* 새로운 유저 정보를 삽입 */
        user.setId(null);
        userRepository.save(user);
    }

    public void Modify(User user){/* 기존 유저 정보를 변경 */
        userRepository.save(user);
    }

    @Transactional/* 지우면 DB 레벨에서 즉시 트랜잭션 */
    public void Delete(User user){/* 기존 유저 정보를 삭제 */
        userRepository.deleteById(user.getId());
    }
}
