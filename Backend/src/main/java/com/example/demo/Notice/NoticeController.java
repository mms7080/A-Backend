package com.example.demo.Notice;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NoticeController {
    private final NoticeService service;

    public NoticeController(NoticeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Notice> list() {
        return service.getAllNotices();
    }

    @PostMapping
    public Notice create(@RequestBody Notice notice) {
        return service.create(notice);
    }

    //공지상세페이지
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //공지작성
    @PostMapping("/create")
        public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        Notice saved = service.create(notice);
            return ResponseEntity.ok(saved);
    }
    //공지삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
