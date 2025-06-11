package com.example.demo.Booking.dto.request;

import com.example.demo.Booking.entity.CustomerCategory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

import java.util.List;
import java.util.Map;

/**
 * 예매 생성을 요청할 때 클라이언트로부터 받는 데이터를 담는 DTO
 */
@Getter
@Setter
@NoArgsConstructor 

public class BookingRequestDto{

    @NotNull(message = "상영 시간표 ID는 필수입니다.")
    private Long showtimeId; 

    @NotEmpty(message = "선택된 좌석이 없습니다.")
    @Size(min = 1, max = 8, message = "좌석은 최소 1개 최대 8개까지 선택 가능합니다.")
    private List<Long> selectedSeatIds;

    @NotNull(message = "고객 유형별 인원 정보는 필수입니다.")
    private Map<CustomerCategory, Integer> customerCounts;
    // 결제 관련 정보 필드 (주석 처리된 부분은 추후 결제 로직 구현 시 구체화)
    // private String paymentKey; // 예: 토스페이먼츠 결제 키
    // private String orderId;    // 예: 클라이언트 또는 서버에서 생성한 주문 ID
    // private Long amount;       // 예: 총 결제 금액 (서버에서 최종 검증 필요)

    

    public BookingRequestDto(Long showtimeId, List<Long> selectedSeatIds, Map<CustomerCategory, Integer> customerCounts) {
        this.showtimeId = showtimeId;
        this.selectedSeatIds = selectedSeatIds;
        this.customerCounts = customerCounts;
        
    }
}
