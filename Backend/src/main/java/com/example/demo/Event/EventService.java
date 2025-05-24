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


    // 카테고리별로 그룹화된 이벤트 목록 반환


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

                //  대표 이미지 1장만 사용 (썸네일 용도)
                String previewImage = event.getImages().isEmpty() ? "" : event.getImages().get(0);
                map.put("image", previewImage);

                return map;
            }, Collectors.toList())
        ));
    }

    // 전체 이벤트 반환 (상세 페이지에서 사용)

    public List<Event> getRawEvents() {
        return repo.findAll();
    }


    // 테스트용 초기 데이터 삽입
    // 서버 시작 시 자동 실행

    @PostConstruct
    public void insertTestData() {
        if (repo.count() > 0) return;

        List<Event> events = List.of(
            new Event(null, "[필모라X진에어] 가정의 달, 할인에 메X진하다!", "2025.05.19 ~ 2025.06.30", "제휴/할인", List.of("/images/event1.jpg")),
            new Event(null, "헬로메이플 가입하고 관람권 받자!", "2025.05.01 ~ 2025.11.30", "제휴/할인", List.of("/images/event2.png")),
            new Event(null, "[토스페이] 5월 토스 결제 프로모션", "2025.05.10 ~ 2025.05.19", "제휴/할인", List.of("/images/event3.jpg")),
            new Event(null, "네이버페이로 결제하고 푸짐한 혜택받기", "2025.05.10 ~ 2025.05.19", "제휴/할인", List.of("/images/event4.jpg")),

            new Event(null, "<소주전쟁> 100% 당첨 스낵전쟁 이벤트", "2025.05.24 ~ 2025.06.08", "Pick", List.of("/images/pick1.jpg", "/images/pick1.jpg")),
            new Event(null, "포스터 증정 이벤트", "2025.06.01 ~ 2025.06.15", "Pick", List.of("/images/pick2.jpg")),
            new Event(null, "코엑스 그랜드 리뉴얼", "2025.06.10 ~ 2025.07.01", "Pick", List.of("/images/pick3.jpg")),
            new Event(null, "<그리드맨 유니버스> 포스터 증정 이벤트", "2025.06.15 ~ 2025.06.30", "Pick", List.of("/images/pick4.jpg")),

            new Event(null, "[목동] MOVIE 콘서트", "2025.06.15 ~ 2025.06.30", "극장", List.of("/images/m1.jpg")),
            new Event(null, "[스타필드] 재즈 나잇", "2025.06.15 ~ 2025.06.30", "극장", List.of("/images/m2.jpg")),

            new Event(null, "<366일> 내한 무대인사", "2025.06.15 ~ 2025.06.30", "시사회/무대인사", List.of("/images/s1.jpg")),
            new Event(null, "<하이파이브> 무대인사","2025.06.15 ~ 2025.06.30", "시사회/무대인사", List.of("/images/s2.jpg"))
        );

        repo.saveAll(events);
        System.out.println("✅ 초기 이벤트 데이터 삽입 완료");
    }
}
