package com.example.demo.FindIDandPW;

import java.util.HashMap;
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
public class find_idLogicController {
    
    @Autowired
    DAOUser daoUser;

    @PostMapping("/find_id/logic")
    public Map<String, String> find_id_logic(@RequestBody Map<String, String> data)/* 아이디 찾기 로직 작성 */
    {
        String name = data.get("name");
        String email = data.get("email");
        String phone = data.get("phone_number");
        String method = data.get("method");

        User user=null;

        if(method.equals("email"))/* 이메일을 활용한 찾기 */
            user=daoUser.findNameEmail(name,email);
        else if(method.equals("phone_number"))/* 휴대폰 번호를 활용한 찾기 */
            user=daoUser.findNamePhone(name,phone);

        Map<String, String> response = new HashMap<>();
        if(user==null)response.put("foundID", "");
        else response.put("foundID",user.getUsername().substring(0, user.getUsername().length() - 2)+"**");

        return response;
    }

}
