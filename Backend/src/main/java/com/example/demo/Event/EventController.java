package com.example.demo.Event;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @GetMapping
    public Map<String, List<Map<String, String>>> getEvents() {
        Map<String, List<Map<String, String>>> result = new HashMap<>();

        result.put("추천", List.of(
            Map.of("title", "KT 멤버십 전용 할인 이벤트", "date", "2025.04.01 ~ 2025.12.31", "image", "/images/event1.jpg"),
            Map.of("title", "LG U+ 고객 특별 할인", "date", "2025.05.01 ~ 2025.11.30", "image", "/images/event2.jpg"),
            Map.of("title", "CGV 포인트 페이백 행사", "date", "2025.05.10 ~ 2025.06.30", "image", "/images/event3.jpg"),
            Map.of("title", "씨네21 X 메가박스 컬래버 이벤트", "date", "2025.06.01 ~ 2025.06.20", "image", "/images/event4.jpg")
        ));

        result.put("메가Pick", List.of(
            Map.of("title", "빼앗긴 거리", "date", "2025.05.24 ~ 2025.06.08", "image", "/images/megapick1.jpg"),
            Map.of("title", "메가박스 개봉작 프리뷰 시사회", "date", "2025.06.01 ~ 2025.06.15", "image", "/images/megapick1.jpg"),
            Map.of("title", "극장판 나의 히어로 아카데미아 특집", "date", "2025.06.10 ~ 2025.07.01", "image", "/images/megapick1.jpg"),
            Map.of("title", "6월 추천 명작 상영회", "date", "2025.06.15 ~ 2025.06.30", "image", "/images/megapick1.jpg")
        ));

        return result;
    }
}
