package com.example.demo.Store;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreRepository repo;

    @Value("${store.upload.dir}")
    private String uploadDir;

    public StoreService(StoreRepository repo) {
        this.repo = repo;
    }

    public Map<String, List<Store>> getGroupedByCategory() {
        return repo.findAll().stream()
                   .collect(Collectors.groupingBy(Store::getCategory));
    }

    public Store findById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다. id=" + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public Store saveWithImage(
        String category,
        String title,
        String subtitle,
        String price,
        String originalPrice,
        String badge,
        String badgeColor,
        MultipartFile image
    ) throws IOException {
        System.out.println("🔥 [StoreService] saveWithImage() 호출됨");

        // 1. 확장자 및 파일명
        String ext = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + ext;

        // 2. 경로 보장
        Path dirPath = Paths.get("src/main").toAbsolutePath().getParent().getParent().resolve(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            System.out.println("📁 디렉토리 생성됨: " + dirPath.toAbsolutePath());
        }

        // 3. 파일 저장
        Path filePath = dirPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("✅ 이미지 저장 완료: " + filePath.toAbsolutePath());

        // 4. 객체 생성 및 저장
        Store s = new Store();
        s.setCategory(category);
        s.setTitle(title);
        s.setSubtitle(subtitle);
        s.setPrice(price);
        s.setOriginalPrice(originalPrice);
        s.setBadge(badge);
        s.setBadgeColor(badgeColor);
        s.setImgUrl("/images/store/" + filename);

        Store saved = repo.save(s);
        System.out.println("✅ DB 저장 완료: id = " + saved.getId());

        return saved;
    }

    @PostConstruct
    public void initDummyStoreItems() {
        if (repo.count() == 0) {
            List<Store> items = List.of(
                new Store(null, "티켓", "일반관람권", "일반 관람권", "13000", null, "대표상품", "black", "/images/ticket.png"),
                new Store(null, "티켓", "VIP 전용관람권", "Dolby Cinema 전용관람권", "18000", null, "추천", "#1e88e5", "/images/ticket2.png"),
                new Store(null, "티켓", "더 부티크 관람권", "더 부티크 전용 관람권", "15000", "16000", null, null, "/images/ticket2.png"),
                new Store(null, "티켓", "더 부티크 스위트 관람권", "더 부티크 스위트 전용관람권", "40000", null, "추천", "#1e88e5", "/images/ticket2.png"),
                new Store(null, "팝콘/음료/콤보", "더블콤보", "팝콘(R) 2 + 탄산음료(R) 2", "13900", null, "BEST", "red", "/images/corn.png"),
                new Store(null, "팝콘/음료/콤보", "러브콤보", "팝콘(L) 1 + 탄산음료(R) 2", "10900", "11900", "BEST", "red", "/images/lovecorn.png"),
                new Store(null, "포인트몰", "1천원 할인쿠폰", "일반/VIP 사용가능", "900", "1000", null, null, "/images/coupon.png")
            );
        
            repo.saveAll(items);
            System.out.println("🎯 더미 데이터 초기화 완료");
        }
    }
}
