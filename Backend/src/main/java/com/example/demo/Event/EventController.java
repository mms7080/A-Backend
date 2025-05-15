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
            Map.of("title", "쿠팡와우카드 전용 영화 할인", "date", "2025.04.01 ~ 2025.12.31", "image", "/images/event1.jpg"),
            Map.of("title", "신한 Edu Plan 영화 7천원", "date", "2025.05.01 ~ 2025.12.31", "image", "/images/event2.jpg")
        ));

        result.put("메가Pick", List.of(
            Map.of("title", "그리드맨 유니버스", "date", "2025.05.24 ~ 2025.06.08", "image", "/images/megapick1.jpg")
        ));

        // 생략: 영화 / 극장 / 제휴 / 시사회 카테고리도 비슷하게 추가

        return result;
    }
}