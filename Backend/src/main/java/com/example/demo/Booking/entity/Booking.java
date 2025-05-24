package com.example.demo.Booking.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.Payment.Payment;
import com.example.demo.User.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 사용자 예매 정보를 총괄하는 엔티티
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    // 예매를 진행한 사용자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // 예매 대상이 된 상영시간 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    // 예매 사용자가 선택한 좌석 목록
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_selected_seats",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> selectedSeats = new ArrayList<>();

    // 고객 유형(CustomerCategory)별로 선택한 인원수를 저장하는 맵(Map)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "booking_passenger_counts", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "customer_category") 
    @MapKeyEnumerated(EnumType.STRING) 
    @Column(name = "count") 
    private Map<CustomerCategory, Integer> passengerCounts = new HashMap<>();

    // 예매의 총 결제 금액
    private Double totalPrice;

    //예매의 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // 예매가 시도되거나 최종적으로 확정된 시간
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    // 예매와 관련된 결제 정보
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", unique = true)
    private Payment payment;
    
}
