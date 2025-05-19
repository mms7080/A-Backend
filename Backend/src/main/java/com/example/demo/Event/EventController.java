package com.example.demo.Event;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService service;
    private final EventRepository repository;

    public EventController(EventService service, EventRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public Map<String, List<Map<String, String>>> getEvents() {
        return service.getEventsGroupedByCategory();
    }

    @GetMapping("/raw")
    public List<Event> getAllEvents() {
        return service.getRawEvents();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadEvent(
            @RequestParam("title") String title,
            @RequestParam("date") String date,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            // 1. 파일명 설정
            String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());

            // 2. 저장할 위치: 프로젝트 루트 기준 uploads/event 폴더
            Path uploadPath = Paths.get("uploads/event").toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 3. 파일 저장
            Path filePath = uploadPath.resolve(filename);
            image.transferTo(filePath.toFile());

            // 4. 이미지 URL 설정 → 프론트에서 접근할 수 있는 경로로
            String imageUrl = "/images/event/" + filename;

            // 5. DB에 저장
            Event event = new Event(null, title, date, imageUrl, category);
            repository.save(event);

            return ResponseEntity.ok("업로드 성공");

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("저장 실패: " + e.getMessage());
        }
    }
}
