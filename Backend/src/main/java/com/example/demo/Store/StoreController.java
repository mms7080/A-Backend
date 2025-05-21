package com.example.demo.Store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    //  카테고리별 상품 리스트 반환
    @GetMapping
    public Map<String, List<Store>> getGroupedItems() {
        return service.getGroupedByCategory();
    }

    // 상품 상세 조회
    @GetMapping("/detail/{id}")
    public Store getItemById(@PathVariable Long id) {
        return service.findById(id);
    }

    //  상품 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    //  상품 업로드 (이미지 포함)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Store uploadItem(
        @RequestParam String category,
        @RequestParam String title,
        @RequestParam(required = false) String subtitle,
        @RequestParam String price,
        @RequestParam(required = false) String originalPrice,
        @RequestParam(required = false) String badge,
        @RequestParam(required = false) String badgeColor,
        @RequestPart("image") MultipartFile image
    ) throws IOException {
        return service.saveWithImage(category, title, subtitle, price, originalPrice, badge, badgeColor, image);
    }
}
