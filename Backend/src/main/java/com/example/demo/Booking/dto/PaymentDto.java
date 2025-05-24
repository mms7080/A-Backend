package com.example.demo.Booking.dto;

import com.example.demo.Payment.Payment; // 기존 Payment 엔티티 참조
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제(Payment) 정보를 클라이언트에게 전달하거나, 다른 DTO 내에 포함될 때 사용하는 DTO 클래스.
 * 기존 Payment 엔티티의 필드들을 대부분 포함하며, 민감한 정보는 제외하거나 마스킹 처리할 수 있다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private String paymentKey; // 결제 시스템에서 발급하는 고유 결제 키
    private String orderId;    // 이 시스템에서의 주문 ID (Booking ID와 동일하게 사용 가능)
    private Integer amount;    // 실제 결제된 금액
    private String orderName;  // 주문명 (예: "영화 '범죄도시4' 외 1건")
    private String status;     // 결제 상태 (예: "DONE", "CANCELED" - 결제 시스템에서 사용하는 상태값)
    private String approvedAt; // 결제 승인 시간 (ISO 8601 형식 문자열)
    private String method;     // 결제 수단 (예: "카드", "가상계좌")
    private String cardCompany; // 카드 결제 시 카드사 정보
    private String cardNumber;  // 카드 결제 시 카드 번호 (일부 마스킹 처리된 형태)

    /**
     * Payment 엔티티 객체를 PaymentDto 객체로 변환하는 정적 팩토리 메서드.
     * @param payment 변환할 Payment 엔티티 객체
     * @return 변환된 PaymentDto 객체. payment가 null이면 null을 반환.
     */
    public static PaymentDto fromEntity(Payment payment) { //
        if (payment == null) {
            return null;
        }
        return new PaymentDto(
                payment.getPaymentKey(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getOrderName(),
                payment.getStatus(),
                payment.getApprovedAt(),
                payment.getMethod(),
                payment.getCardCompany(),
                payment.getCardNumber() // 실제로는 마스킹된 카드번호를 저장/반환하는 것이 좋음
        );
    }
}