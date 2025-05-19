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
            @RequestParam("images") List<MultipartFile> images
    ) {
        try {
            List<String> imageUrls = new ArrayList<>();
            Path uploadPath = Paths.get("uploads/event").toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

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

    @GetMapping("/view/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Optional<Event> optionalEvent = repository.findById(id);
        return optionalEvent
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
