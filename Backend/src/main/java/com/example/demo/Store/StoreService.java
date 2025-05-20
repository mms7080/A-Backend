package com.example.demo.Store;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreRepository repo;

    public StoreService(StoreRepository repo) {
        this.repo = repo;
    }

    public List<Store> getAll() {
        return repo.findAll();
    }

    public Map<String, List<Store>> getGroupedByCategory() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Store::getCategory));
    }

    public Store save(Store s) {
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ 초기 더미 스토어 상품 등록
    @PostConstruct
    public void initDummyStoreItems() {
        if (repo.count() == 0) {
            List<Store> items = List.of(
                new Store(null, "티켓", "일반관람권", "일반 관람권", "13,000원", null, "대표상품", "black", "/images/ticket.png"),
                new Store(null, "티켓", "Dolby Cinema 전용관람권", "Dolby Cinema 전용관람권", "18,000원", null, "추천", "#1e88e5", "/images/ticket.png"),
                new Store(null, "티켓", "더 부티크 관람권", "더 부티크 전용 관람권", "15,000원", "16,000원", null, null, "/images/ticket.png"),
                new Store(null, "티켓", "더 부티크 스위트 관람권", "더 부티크 스위트 전용관람권", "40,000원", null, "추천", "#1e88e5", "/images/ticket.png"),
                new Store(null, "팝콘/음료/콤보", "더블콤보", "팝콘(R) 2 + 탄산음료(R) 2 ", "13.900원", null, "추천", "#1e88e5", "/images/corn.png"),
                new Store(null, "팝콘/음료/콤보", "러브콤보", "팝콘(L) 1 + 탄산음료(R) 2 ", "10.900원", null, "추천", "#1e88e5", "/images/lovecorn.png")
            );
            repo.saveAll(items);
        }
    }

}
