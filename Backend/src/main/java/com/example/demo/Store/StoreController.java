package com.example.demo.Store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Payment.Payment;
import com.example.demo.Payment.PaymentRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StoreController {

    private final StoreService service;
    private final StoreRepository storeRepo;
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;

    public StoreController(StoreService service,
            StoreRepository storeRepo,
            PaymentRepository paymentRepo,
            UserRepository userRepo) {
        this.service = service;
        this.storeRepo = storeRepo;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
    }

    // 카테고리별 상품 리스트 반환
    @GetMapping
    public Map<String, List<Store>> getGroupedItems() {
        return service.getGroupedByCategory();
    }

    // 상품 상세 조회
    @GetMapping("/detail/{id}")
    public Store getItemById(@PathVariable Long id) {
        return service.findById(id);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 상품 업로드 (이미지 포함)
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
            @RequestPart("image") MultipartFile image) throws IOException {
        return service.saveWithImage(category, title, subtitle, price, originalPrice, badge, badgeColor, image);
    }

    // ✅ 랜덤박스 열기 API
    @PostMapping("/random-box")
    public Map<String, Object> openRandomBox(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();

        // 70% 확률로 꽝
        if (new Random().nextInt(100) >= 30) {
            response.put("result", "꽝입니다! 다음 기회에~");
            return response;
        }

        // 포인트몰 상품 목록 가져오기
        List<Store> pointMallItems = storeRepo.findByCategory("포인트몰");
        if (pointMallItems.isEmpty()) {
            response.put("result", "당첨 상품이 없습니다.");
            return response;
        }

        Store prize = pointMallItems.get(new Random().nextInt(pointMallItems.size()));

        // 유저 정보 확인
        User user = userRepo.findByUsername(userId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 유저 없음"));

        // Payment 레코드 생성
        Payment payment = new Payment();
        payment.setPaymentKey("RANDOM-" + UUID.randomUUID());
        payment.setOrderId("randombox-" + UUID.randomUUID());
        payment.setAmount(0);
        payment.setUserId(userId);
        payment.setOrderName(prize.getTitle());
        payment.setStatus("WON");
        payment.setApprovedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        payment.setMethod("랜덤박스");
        payment.setCardCompany(null);
        payment.setCardNumber(null);

        paymentRepo.save(payment);

        response.put("result", "당첨!");
        response.put("item", prize);
        return response;
    }
}
