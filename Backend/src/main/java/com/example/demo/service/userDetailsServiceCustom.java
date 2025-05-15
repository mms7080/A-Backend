package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.classes.User;
import com.example.demo.classes.UserInfo;
import com.example.demo.dao.DAOUser;
import com.example.demo.util.RandomUtil;

@Service
public class userDetailsServiceCustom extends DefaultOAuth2UserService implements UserDetailsService{

    @Autowired
    DAOUser daoUser;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{/* OAuth 로그인을 위한 파트 */
        OAuth2User oauth2user=super.loadUser(userRequest);
        String provider=userRequest.getClientRegistration().getClientName();
        String getid="id";
        String name="";
        String platform=null;
        Map<String,Object> response=null;

        switch(provider){
            case "naver":/* 네이버 로그인의 경우를 처리 */
                response=oauth2user.getAttribute("response");
                getid="id";
                name = (String)response.get("name");/* 네이버 로그인의 경우 본명을 가져옴 */
                platform="naver";
                break;
            case "kakao":/* 카카오 로그인의 경우를 처리 */
                response=oauth2user.getAttributes();
                getid="id";
                Map<String, Object> kakao_account = (Map<String, Object>) response.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
                name = (String) profile.get("nickname");/* 카카오 로그인의 경우 닉네임을 가져옴(본명을 가져오는 건 불가능) */
                platform="kakao";
                break;
            case "Google":/* 구글 로그인의 경우를 처리 */
                response=oauth2user.getAttributes();
                getid="sub";
                name = (String) response.get("name");/* 구글 로그인의 경우 본명을 가져옴 */
                platform="google";
                break;
        }

        User newuser=new User(
                null,
                String.valueOf(response.get(getid)),
                new BCryptPasswordEncoder().encode(RandomUtil.getRandomString(RandomUtil.getRandomInteger(15,25))),/* OAuth2 유저의 경우 임의의 문자열을 생성해서 암호화 한 후 DB에 저장하게 함 */
                name,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of("USER"),
                platform
        );
        
        User user = daoUser.findUsername(newuser.getUsername());/* OAuth로 로그인한 사용자 정보가 DB에 있는지 확인 */
        if(user==null)daoUser.Insert(newuser);/* DB에 OAuth로 로그인한 유저정보가 없을 경우 새로 삽입 */

        return new UserInfo(newuser);/* 소셜 로그인을 통해 로그인한 사용자의 정보를 처리 */
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{/* 스프링 security에서 ID를 조회하고 UserInfo로 변환 */
        User user = daoUser.findUsername(username);
        if(user==null) throw new UsernameNotFoundException(username);
        return new UserInfo(user);
    }

}