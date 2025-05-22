package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.ReservationDto;
import com.example.demo.Booking.entity.Reservation;
import com.example.demo.Booking.service.ReservationService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/booking/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    // 모든 예약 목록 조회(관리자/ 테스트 용도)
    // booking/reservations
    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return reservationService.findAll().stream()
                .map(ReservationDto::fromEntity) // Reservation 엔티티를 ReservationDto로 변환
                .collect(Collectors.toList()); // 리스트로 수집하여 반환
    }
    
    // ID를 가진 예약 상세 정보를 조회
    // booking/reservations/{id}
    @GetMapping("/{id}")
    public ReservationDto getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getById(id); // 서비스에서 예약 조회
        return ReservationDto.fromEntity(reservation); // 엔티티를 DTO로 변환하여 반환
    }
    
    // 새로운 예약을 생성
    // /booking/reservations
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@RequestBody ReservationDto dto){
        Reservation toCreate = dto.toEntity(); // DTO를 Reservation 엔티티로 변환
        Reservation created = reservationService.create(toCreate); // 서비스에서 예약 생성
        return ReservationDto.fromEntity(created); // 생성된 엔티티를 DTO로 변환하여 반환
    }

    // 기존 예약을 수정
    // booking/reservations/{id}
    @PutMapping("/{id}")
    public ReservationDto updateReservationDto(@PathVariable Long id,
                                                @RequestBody ReservationDto dto) {
       Reservation toUpdate = dto.toEntity();
       Reservation updated = reservationService.update(id, toUpdate); // 서비스에서 예약 수정
       return ReservationDto.fromEntity(updated);
    }
    
    // 특정 ID를 가진 예약을 삭제
    // booking/reservations/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteReservation(@PathVariable Long id){
            reservationService.delete(id); // 서비스에서 예약 삭제
        }
    
    // 고객 이름으로 예약 검색
    // booking/reservations/search/by-customer-name?name={customerName}
    @GetMapping("/search/by-customer-name")
    public List<ReservationDto> findReservationsByCustomerName(@RequestParam String name) {
        return reservationService.findByCustomerName(name).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 좌석 ID가 포함된 예약을 검색
    // booking/reservations/search/by-seat-id?seatId={seatId}
    @GetMapping("/search/by-seat-id")
    public List<ReservationDto> findReservationsBySeatId(@RequestParam Long seatId) {
        return reservationService.findBySeatId(seatId).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 지정된 예약 시간 범위 내의 예약을 검색
    // booking/reservations/search/by-time-range?from={isoDateTime}&to={isoDateTime}
    // 날짜/시간 형식은 ISO 8601 (예: 2023-10-26T10:00:00)을 사용
    @GetMapping("/search/by-time-range")
    public List<ReservationDto> findReservationsByReservedAtBetween(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return reservationService.findByReservedAtBetween(from, to).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }
}


