package com.example.demo.Home;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Annotations.Auth;
import com.example.demo.User.User;

@Controller
public class homeController{

    @GetMapping("/home")
    public String home(@Auth User user,Model model){
        model.addAttribute("isLogin",user!=null);/* 현재 접속 중인 user 변수가 null인지 아닌지에 따라 isLogin 설정 */
        if(user!=null){
            model.addAttribute("user",user);/* 유저의 이름 또는 닉네임을 홈 화면에 표시하기 위해 값 설정  */
            if(user.getPlatform()!= null)model.addAttribute("oauth2",true);/* OAuth2로 로그인한 사용자인지 판별해서 마이페이지 들어가지 못하게 하기 위해 설정 */
            else model.addAttribute("oauth2",false);
        }
        return "home";
    }


    @ResponseBody
    @GetMapping("/images/event/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        Path uploadPath = Paths.get("src/main/").toAbsolutePath().getParent().getParent().resolve("./images/event").resolve(id);
        System.out.println(uploadPath.toString());
        if (Files.notExists(uploadPath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 파일이 존재하지 않으면 404 반환
        }
        
        try (InputStream in = Files.newInputStream(uploadPath)) {
            // 파일의 MIME 타입을 추정하여 Content-Type 설정
            String contentType = Files.probeContentType(uploadPath);
            
            // 기본적인 이미지 Content-Type 설정 (혹시 파일 타입을 추론할 수 없을 때)
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            // 이미지 바이트 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(in.readAllBytes());
        } catch (IOException e) {
            // 파일 읽기 오류 발생 시 500 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @GetMapping("/images/store/{id}")
    public ResponseEntity<byte[]> getImageStore(@PathVariable("id") String id) {
        Path uploadPath = Paths.get("src/main/").toAbsolutePath().getParent().getParent().resolve("./images/store").resolve(id);
        System.out.println(uploadPath.toString());
        if (Files.notExists(uploadPath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 파일이 존재하지 않으면 404 반환
        }
        
        try (InputStream in = Files.newInputStream(uploadPath)) {
            // 파일의 MIME 타입을 추정하여 Content-Type 설정
            String contentType = Files.probeContentType(uploadPath);
            
            // 기본적인 이미지 Content-Type 설정 (혹시 파일 타입을 추론할 수 없을 때)
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            // 이미지 바이트 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(in.readAllBytes());
        } catch (IOException e) {
            // 파일 읽기 오류 발생 시 500 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}