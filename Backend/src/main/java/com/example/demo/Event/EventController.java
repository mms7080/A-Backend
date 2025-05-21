package com.example.demo.Event;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${event.upload.dir}")
    private String uploadDir;

    public EventController(EventService service, EventRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // 이벤트 조회 
    @GetMapping
    public Map<String, List<Map<String, String>>> getEvents() {
        return service.getEventsGroupedByCategory();
    }

    // 모든 이벤트 Raw 데이터 반환 (상세 페이지, 수정용)
    @GetMapping("/raw")
    public List<Event> getAllEvents() {
        return service.getRawEvents();
    }

    // 특정 ID의 이벤트 상세 조회
    @GetMapping("/view/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Optional<Event> optionalEvent = repository.findById(id);
        return optionalEvent
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  이벤트 업로드 API
    @PostMapping("/upload")
    public ResponseEntity<?> uploadEvent(
            @RequestParam("title") String title,
            @RequestParam("date") String date,
            @RequestParam("category") String category,
            @RequestParam("images") List<MultipartFile> images
    ) {
        try {
            List<String> imageUrls = new ArrayList<>();

            // 이미지 저장 경로
            Path uploadPath = Paths.get("src/main").toAbsolutePath().getParent().getParent().resolve(uploadDir);
            System.out.println(uploadPath.toString());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 이미지 저장 및 URL 구성
            for (MultipartFile image : images) {
                String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
                Path filePath = uploadPath.resolve(filename);
                image.transferTo(filePath.toFile());
                imageUrls.add("/images/event/" + filename);
            }

            Event event = new Event(null, title, date, category, imageUrls);
            repository.save(event);
            return ResponseEntity.ok("업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("저장 실패: " + e.getMessage());
        }
    }

    // 이벤트 삭제 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok("삭제 완료");
    }
}