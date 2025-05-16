package com.example.demo.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByUsername(String username);/* 유저 ID 기반으로 찾는 함수 */
    
    public Optional<User> findByUsernameAndNameAndEmail(String username,String name,String email);/* 유저 ID, 이름, 이메일 기반으로 찾는 함수 */

    public Optional<User> findByUsernameAndNameAndPhone(String username,String name,String phone);/* 유저 ID, 이름, 전화번호 기반으로 찾는 함수 */

    public Optional<User> findByNameAndEmail(String name,String email);/* 유저 이름, 이메일 기반으로 찾는 함수 */
    
    public Optional<User> findByNameAndPhone(String name,String phone);/* 유저 이름, 전화번호 기반으로 찾는 함수 */

    boolean existsByUsername(String username); /* 유저 ID가 존재하나 여부를 검증하는 함수 */

}
