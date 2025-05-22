package com.example.demo.Booking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Booking.entity.Reservation;
import com.example.demo.Booking.repository.ReservationRepository;

@Repository
public class ReservationDao {
    private final ReservationRepository repo;
    
    // 생성자 주입
    public ReservationDao(ReservationRepository repo){
        this.repo = repo;
    }

    // 모든 예약 조회
    public List<Reservation> findAll(){
        return repo.findAll();
    }

    // 특정 ID로 예약 조회 / 없으면 예외 발생
    public Reservation findById(Long id){
        return repo.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 예약 ID:"+ id));
    }
    // 새로운 예약 저장 / 기존 예약 업데이트
    public Reservation save(Reservation reservation){
        return repo.save(reservation);
    }
    // 특정 ID로 삭제
    public void deleteById(Long id){
        repo.deleteById(id);
    }
    
    // 고객 이름으로 예약 검색
    public List<Reservation> findCustomerName(String name){
        return repo.findByCustomerName(name);
    }
    // 특정 좌석 ID가 포함된 예약 검색
    public List<Reservation> findBySeatId(Long seatId){
        return repo.findBySeats_Id(seatId);
    }
    // 지정된 예약 시간 범위 내의 예약 검색
    public List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to){
        return repo.findByReservedAtBetween(from, to);
    }

}
