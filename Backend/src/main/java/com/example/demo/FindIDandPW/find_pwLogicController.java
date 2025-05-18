package com.example.demo.FindIDandPW;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.User.DAOUser;
import com.example.demo.User.User;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")/* 프론트주소 바뀌면 같이 바꾸어 줘어야 함. */
public class find_pwLogicController {
    
    @Autowired
    DAOUser daoUser;

    @PostMapping("/find_pw/logic")
    public Boolean find_id_logic(@RequestBody Map<String, String> data)/* 비밀번호 재설정정 로직 작성 */
    {

        String username= data.get("id");
        String name= data.get("name");
        String email= data.get("email");
        String phone= data.get("phone_number");
        String method= data.get("method");
    
        User user=null;

        if(method.equals("email"))/* 이메일을 활용한 찾기 */
            user=daoUser.findUsernameNameEmail(username,name,email);
        else if(method.equals("phone_number"))/* 휴대폰 번호를 활용한 찾기 */
            user=daoUser.findUsernameNamePhone(username,name,phone);

        return (user!=null);
    }

}
