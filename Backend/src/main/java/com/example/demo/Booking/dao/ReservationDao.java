package com.example.demo.Booking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Booking.entity.Reservation;
import com.example.demo.Booking.repository.ReservationRepository;

@Repository
public class ReservationDao {
    private final ReservationRepository repo;

    public ReservationDao(ReservationRepository repo){
        this.repo = repo;
    }

    // 기본 CRUD
    public List<Reservation> findAll(){
        return repo.findAll();
    }

    public Reservation findById(Long id){
        return repo.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 예약 ID:"+ id));
    }

    public Reservation save(Reservation reservation){
        return repo.save(reservation);
    }

    public void deleteById(Long id){
        repo.deleteById(id);
    }
    
    // 커스텀 메서드
    public List<Reservation> findCustomerName(String name){
        return repo.findByCustomerName(name);
    }

    public List<Reservation> findBySeatId(Long seatId){
        return repo.findBySeats_Id(seatId);
    }

    public List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to){
        return repo.findByReservedAtBetween(from, to);
    }

}
