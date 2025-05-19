package com.example.demo.Event;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public Map<String, List<Map<String, String>>> getEventsGroupedByCategory() {
        List<Event> events = repo.findAll();

        return events.stream().collect(Collectors.groupingBy(
            Event::getCategory,
            Collectors.mapping(event -> {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(event.getId()));
                map.put("title", event.getTitle());
                map.put("date", event.getDate());
                map.put("category", event.getCategory());
                // ✅ 대표 이미지 1장만 프리뷰용으로 보냄
                map.put("image", event.getImages().isEmpty() ? "" : event.getImages().get(0));
                return map;
            }, Collectors.toList())
        ));
    }

    public List<Event> getRawEvents() {
        return repo.findAll();
    }

    @PostConstruct
    public void insertTestData() {
        if (repo.count() > 0) return;

            List<Event> events = List.of(
                new Event(null, "KT 멤버십 전용 할인 이벤트", "2025.04.01 ~ 2025.12.31", "추천", List.of("/images/event1.jpg")),
                new Event(null, "LG U+ 고객 특별 할인", "2025.05.01 ~ 2025.11.30", "추천", List.of("/images/event2.jpg")),
                new Event(null, "CGV 포인트 페이백 행사", "2025.05.10 ~ 2025.06.30", "추천", List.of("/images/event3.jpg")),
                new Event(null, "씨네21 X 메가박스 컬래버 이벤트", "2025.06.01 ~ 2025.06.20", "추천", List.of("/images/event4.jpg")),

                new Event(null, "빼앗긴 거리", "2025.05.24 ~ 2025.06.08", "메가Pick", List.of("/images/megapick1.jpg", "/images/megapick2.jpg")),
                new Event(null, "메가박스 개봉작 프리뷰 시사회", "2025.06.01 ~ 2025.06.15", "메가Pick", List.of("/images/megapick1.jpg")),
                new Event(null, "극장판 나의 히어로 아카데미아 특집", "2025.06.10 ~ 2025.07.01", "메가Pick", List.of("/images/megapick1.jpg")),
                new Event(null, "6월 추천 명작 상영회", "2025.06.15 ~ 2025.06.30", "메가Pick", List.of("/images/megapick1.jpg"))
            );


        repo.saveAll(events);
        System.out.println("✅ 초기 이벤트 데이터 삽입 완료");
    }
}
