package com.example.demo.Store;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StoreController {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    // 전체 상품 카테고리별 조회
    @GetMapping
    public Map<String, List<Store>> getGroupedItems() {
        return service.getGroupedByCategory();
    }

    // 상품 등록
    @PostMapping
    public Store create(@RequestBody Store store) {
        return service.save(store);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ 상품 상세 조회 추가
    @GetMapping("/detail/{id}")
    public Store getItemById(@PathVariable Long id) {
        return service.findById(id);
    }
}
