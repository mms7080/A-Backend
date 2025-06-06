package com.example.demo.OAuthLogin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        //String getid = "id";
        //String name = "";
        //String platform = null;
        Map<String, Object> response = null;

        switch (provider) {
            case "naver" -> {
                response = oauth2user.getAttribute("response");
                if(response == null)
                    throw new OAuth2AuthenticationException("Response is null");
                //getid = "id";
                //name = (String) response.get("name");
                //platform = "naver";
            }
            case "kakao" -> {
                response = oauth2user.getAttributes();
                if(response == null)
                    throw new OAuth2AuthenticationException("Response is null");
                //getid = "id";
                //@SuppressWarnings("unchecked")
                //Map<String, Object> kakao_account = (Map<String, Object>) response.get("kakao_account");
                //@SuppressWarnings("unchecked")
                //Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
                //name = (String) profile.get("nickname");
                //platform = "kakao";
            }
            case "Google" -> {
                response = oauth2user.getAttributes();
                if(response == null)
                    throw new OAuth2AuthenticationException("Response is null");
                //getid = "sub";
                //name = (String) response.get("name");
                //platform = "google";
            }
        }

        if(response == null)
            throw new OAuth2AuthenticationException("Response is null");

        // 이하는 OAuth2 로그인이 모든 이에게 가능할 경우 작성할 코드
        // String username = String.valueOf(response.get(getid));

        // User user = daoUser.findUsername(username); // 중복 처리된 안전한 조회

        // /* 포맷터 정의 (yyyy-MM-dd) */
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // /* 오늘 날짜 가져오기 */
        // LocalDate today = LocalDate.now();

        // /* 포맷 적용 */
        // String formattedDate = today.format(formatter);

        // if (user == null) {
        //     User newuser = new User(
        //         null,
        //         username,
        //         new BCryptPasswordEncoder().encode(RandomUtil.getRandomString(RandomUtil.getRandomInteger(15, 25))),
        //         name,
        //         null, null, null, null, null, null, null,
        //         "USER",
        //         platform,
        //         formattedDate,
        //         null
        //     );
        //     daoUser.Insert(newuser);
        //     user = newuser;
        // }

        // return new UserInfo(user);

        // 이하는 OAuth2 로그인이 관리자 1인에 한해서 가능할 경우 작성할 코드

        // ✅ 로그인된 사용자를 강제로 root로 변경
        User rootUser = daoUser.findUsername("root");
        UserInfo rootInfo = new UserInfo(rootUser);

        // Spring Security 인증 객체 등록 (root 사용자로)
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(rootInfo, null, rootInfo.getAuthorities())
        );

        // root 사용자 정보를 OAuth2User로 반환
        return rootInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = daoUser.findUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new UserInfo(user);
    }
}
