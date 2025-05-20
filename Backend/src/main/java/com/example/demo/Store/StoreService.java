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
                new Store(null, "메가티켓", "일반관람권", "일반 관람권", "13,000원", null, "대표상품", "black", "https://www.megabox.co.kr/static/pc/images/store/img-product-ticket1.png"),
                new Store(null, "메가티켓", "Dolby Cinema 전용관람권", "Dolby Cinema 전용관람권", "18,000원", null, "추천", "#1e88e5", "https://www.megabox.co.kr/static/pc/images/store/img-product-ticket-dolby.png"),
                new Store(null, "메가티켓", "더 부티크 관람권", "더 부티크 전용 관람권", "15,000원", "16,000원", null, null, "https://www.megabox.co.kr/static/pc/images/store/img-product-ticket-boutique.png"),
                new Store(null, "메가티켓", "더 부티크 스위트 관람권", "더 부티크 스위트 전용관람권", "40,000원", null, "추천", "#1e88e5", "https://www.megabox.co.kr/static/pc/images/store/img-product-ticket-suite.png")
            );
            repo.saveAll(items);
        }
    }
}
