package com.example.demo.Notice;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NoticeController {

    private final NoticeService service;

    public NoticeController(NoticeService service) {
        this.service = service;
    }

    // 전체 목록 조회
    @GetMapping
    public List<Notice> list() {
        return service.getAllNotices();
    }

    // 공지 작성 (POST /notice)
    @PostMapping
    public Notice create(@RequestBody Notice notice) {
        return service.create(notice);
    }

    // 공지 작성 (POST /notice/create)
    @PostMapping("/create")
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        Notice saved = service.create(notice);
        return ResponseEntity.ok(saved);
    }

    // ✅ 공지 상세 조회 (조회수 증가 포함)
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        Notice notice = service.getByIdWithViewIncrease(id);
        return notice != null ? ResponseEntity.ok(notice) : ResponseEntity.notFound().build();
    }

    // 공지 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable Long id, @RequestBody NoticeDTO dto) {
        service.update(id, dto);
        return ResponseEntity.ok().build();
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
