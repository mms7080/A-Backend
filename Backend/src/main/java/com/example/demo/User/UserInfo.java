package com.example.demo.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements UserDetails,OAuth2User{

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){/* 유저의 권한을 get */
        List<SimpleGrantedAuthority> auth=new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("ROLE_"+user.getAuth()));
        return auth;
    }

    @Override
    public String getPassword(){/* 유저의 비밀번호를 get */
        return user.getPassword();
    }

    @Override
    public String getUsername(){/* 유저의 ID를 get */
        return user.getUsername();
    }

    @Override
    public String getName(){/* 유저의 ID를 get */
        return user.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes(){/* OAuth2 유저의 속성을 리턴하는 함수는 쓸모가 없으므로 return null */
        return null;
    }

    @Override
    public boolean isAccountNonExpired(){/* UserDetails 메서드 오버라이드 */
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){/* UserDetails 메서드 오버라이드 */
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){/* UserDetails 메서드 오버라이드 */
        return true;
    }

    @Override
    public boolean isEnabled(){/* UserDetails 메서드 오버라이드 */
        return true;
    }

}