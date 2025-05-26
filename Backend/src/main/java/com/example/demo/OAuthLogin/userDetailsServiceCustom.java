package com.example.demo.OAuthLogin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

import com.example.demo.User.DAOUser;
import com.example.demo.User.User;
import com.example.demo.User.UserInfo;

@Service
public class userDetailsServiceCustom extends DefaultOAuth2UserService implements UserDetailsService {

    @Autowired
    DAOUser daoUser;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2user = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getClientName();
        String getid = "id";
        String name = "";
        String platform = null;
        Map<String, Object> response = null;

        switch (provider) {
            case "naver":
                response = oauth2user.getAttribute("response");
                getid = "id";
                name = (String) response.get("name");
                platform = "naver";
                break;
            case "kakao":
                response = oauth2user.getAttributes();
                getid = "id";
                Map<String, Object> kakao_account = (Map<String, Object>) response.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
                name = (String) profile.get("nickname");
                platform = "kakao";
                break;
            case "Google":
                response = oauth2user.getAttributes();
                getid = "sub";
                name = (String) response.get("name");
                platform = "google";
                break;
        }

        String username = String.valueOf(response.get(getid));

        User user = daoUser.findUsername(username); // 중복 처리된 안전한 조회

        /* 포맷터 정의 (yyyy-MM-dd) */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /* 오늘 날짜 가져오기 */
        LocalDate today = LocalDate.now();

        /* 포맷 적용 */
        String formattedDate = today.format(formatter);

        if (user == null) {
            User newuser = new User(
                null,
                username,
                new BCryptPasswordEncoder().encode(RandomUtil.getRandomString(RandomUtil.getRandomInteger(15, 25))),
                name,
                null, null, null, null, null, null, null,
                "USER",
                platform,
                formattedDate

            );
            daoUser.Insert(newuser);
            user = newuser;
        }

        return new UserInfo(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = daoUser.findUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new UserInfo(user);
    }
}
