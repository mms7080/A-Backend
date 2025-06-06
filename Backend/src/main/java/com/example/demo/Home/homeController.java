package com.example.demo.Home;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class homeController{

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

    @ResponseBody
    @GetMapping("/images/movie/{id}")
    public ResponseEntity<byte[]> getImageMovie(@PathVariable("id") String id) {
        Path uploadPath = Paths.get("src/main/").toAbsolutePath().getParent().getParent().resolve("./images/movie").resolve(id);
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