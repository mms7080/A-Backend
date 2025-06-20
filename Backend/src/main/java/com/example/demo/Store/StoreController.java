package com.example.demo.Store;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Payment.Payment;
import com.example.demo.Payment.PaymentRepository;
import com.example.demo.Reservation.Coupon;
import com.example.demo.Reservation.CouponRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StoreController {

    private final StoreService service;
    private final StoreRepository storeRepo;
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;

    public StoreController(StoreService service,
            StoreRepository storeRepo,
            PaymentRepository paymentRepo,
            UserRepository userRepo,
            CouponRepository couponRepo) {
        this.service = service;
        this.storeRepo = storeRepo;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
        this.couponRepo = couponRepo;
    }

    // 📦 카테고리별 상품 리스트
    @GetMapping
    public Map<String, List<Store>> getGroupedItems() {
        return service.getGroupedByCategory();
    }

    // 🧾 상품 상세 조회
    @GetMapping("/detail/{id}")
    public Store getItemById(@PathVariable Long id) {
        return service.findById(id);
    }

    // ❌ 상품 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 🖼️ 상품 업로드
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

    // 🎲 랜덤박스 열기
    @PostMapping("/random-box")
    public Map<String, Object> openRandomBox(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();

        if (new Random().nextInt(100) >= 50) {
            response.put("result", "꽝입니다! 다음 기회에~");
            return response;
        }

        List<Store> pointMallItems = storeRepo.findByCategory("포인트몰");
        if (pointMallItems.isEmpty()) {
            response.put("result", "당첨 상품이 없습니다.");
            return response;
        }

        Store prize = pointMallItems.get(new Random().nextInt(pointMallItems.size()));

        User user = userRepo.findByUsername(userId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 유저 없음"));

        Payment payment = new Payment();
        payment.setPaymentKey("RANDOM-" + UUID.randomUUID());
        payment.setOrderId("randombox-" + UUID.randomUUID());
        payment.setAmount(0);
        payment.setUserId(userId);
        payment.setOrderName(prize.getTitle());
        payment.setStatus("WON");
        payment.setApprovedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        payment.setMethod("랜덤박스");

        paymentRepo.save(payment);

        // ⭐ 랜덤박스 쿠폰 지급
        issueCouponIfNeeded(prize.getTitle(), user.getUsername(), "랜덤박스"); // 반드시 username!

        response.put("result", "당첨!");
        response.put("item", prize);
        return response;
    }

    @PostMapping("/purchase/success")
    public ResponseEntity<?> onStorePurchaseSuccess(@RequestBody Map<String, String> payload) {
        String username = payload.get("username"); // ✅ 키 이름 수정
        String itemTitle = payload.get("title");

        if (username == null || itemTitle == null) {
            return ResponseEntity.badRequest().body("필수 정보 누락");
        }

        // username으로 바로 조회
        Optional<User> userOpt = userRepo.findByUsername(username).stream().findFirst();
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("유저를 찾을 수 없습니다");
        }

        issueCouponIfNeeded(itemTitle, username, "스토어 구매");
        return ResponseEntity.ok("쿠폰 발급 완료");
    }

    // 🎟️ 쿠폰 발급 공통 로직
    private void issueCouponIfNeeded(String title, String userId, String source) {
        Coupon coupon = new Coupon();
        coupon.setUserId(userId); // 반드시 username
        coupon.setUsed(false);

        // 🎟️ 관람권 상품 → 쿠폰 발급 분기
        if (title.contains("일반관람권")) {
            coupon.setType("GENERAL_TICKET");
            coupon.setDiscountAmount(15000);
            coupon.setDescription(source + " 일반 관람권");
        } else if (title.contains("VIP 일반관람권")) {
            coupon.setType("GENERAL_TICKET");
            coupon.setDiscountAmount(20000);
            coupon.setDescription(source + " VIP 일반 관람권");
        } else if (title.contains("더 부티크관람권")) {
            coupon.setType("GENERAL_TICKET");
            coupon.setDiscountAmount(30000);
            coupon.setDescription(source + " 더 부티크 관람권");
        } else if (title.contains("더 스위트관람권")) {
            coupon.setType("GENERAL_TICKET");
            coupon.setDiscountAmount(40000);
            coupon.setDescription(source + " 더 스위트 관람권");

            // 🎫 포인트몰 할인쿠폰
        } else if (title.contains("1천원 할인쿠폰")) {
            coupon.setType("DISCOUNT");
            coupon.setDiscountAmount(1000);
            coupon.setDescription(source + " 1,000원 할인 쿠폰");

        } else {
            return; // 해당 없음 → 쿠폰 발급 X
        }

        couponRepo.save(coupon);
        System.out.println("✅ 쿠폰 발급 완료: " + coupon);
    }

    // 쿠폰 사용처리
    @PostMapping("/use-coupon")
    public ResponseEntity<?> useCoupon(@RequestBody Map<String, Object> payload) {
        Long couponId = Long.parseLong(payload.get("couponId").toString());

        Optional<Coupon> optionalCoupon = couponRepo.findById(couponId);
        if (optionalCoupon.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 쿠폰이 존재하지 않습니다.");
        }

        Coupon coupon = optionalCoupon.get();
        if (coupon.isUsed()) {
            return ResponseEntity.badRequest().body("이미 사용된 쿠폰입니다.");
        }

        coupon.setUsed(true);
        couponRepo.save(coupon);

        return ResponseEntity.ok("쿠폰 사용 처리 완료");
    }

    // 장바구니 구매
    @PostMapping("/purchase/cart/success")
    public ResponseEntity<?> onCartPurchaseSuccess(@RequestBody Map<String, Object> payload) {
        String username = payload.get("userId").toString();
        String orderId = payload.get("orderId").toString();
        String paymentKey = payload.get("paymentKey").toString();
        int totalAmount = Integer.parseInt(payload.get("amount").toString());
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

        Optional<User> userOpt = userRepo.findByUsername(username).stream().findFirst();
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "유저를 찾을 수 없습니다"));
        }

        User user = userOpt.get();
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        Map<String, Object> response = new HashMap<>();

        // 첫 번째 상품 정보만 대표로 응답에 담기
        if (!items.isEmpty()) {
            Map<String, Object> firstItem = items.get(0);
            response.put("orderName", firstItem.get("title"));
            response.put("amount", totalAmount);
            response.put("method", "스토어 장바구니");
            response.put("orderId", orderId);
            response.put("approvedAt", now.format(formatter));
            response.put("cardCompany", "TossPayments");
            response.put("cardNumber", "****-****-****-1234");
        }

        for (Map<String, Object> item : items) {
            String title = item.get("title").toString();
            int price = Integer.parseInt(item.get("price").toString());

            Payment payment = new Payment();
            payment.setPaymentKey(paymentKey + "-" + UUID.randomUUID());
            payment.setOrderId(orderId + "-" + UUID.randomUUID());
            payment.setAmount(price);
            payment.setUserId(username);
            payment.setOrderName(title);
            payment.setStatus("DONE");
            payment.setApprovedAt(now.format(formatter));
            payment.setMethod("스토어 장바구니");

            paymentRepo.save(payment);
            issueCouponIfNeeded(title, username, "스토어 장바구니");
        }

        return ResponseEntity.ok(response);
    }

}
