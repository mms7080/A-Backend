package com.example.demo.Booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dao.ReservationDao;
import com.example.demo.Booking.entity.Reservation;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;

@Service
public class ReservationService {
    private final ReservationDao dao;
    private final SeatService seatService;

    public ReservationService(ReservationDao dao, SeatService seatService){
        this.dao = dao;
        this.seatService = seatService;
    }

    // ID로 예약 조회
    @Transactional(readOnly = true)
    public Reservation getById(Long id){
        return dao.findById(id);
    }

    // 새 예약 생성
    // 예약 시간이 지정되지 않았다면 현재 시간으로 설정
    // 연관된 좌석들이 상태를 AVAILABLE → RESERVED 로 변경
    // 최종적으로 예약을 저장
    @Transactional 
    public Reservation create(Reservation reservation){
        // 예약시간 자동 설정
        if(reservation.getReservedAt() == null){
            reservation.setReservedAt(LocalDateTime.now());
        }

        // 포함된 모든 좌석을 불러와서 사용 가능 여부 검사 및 상태 변경
        for(Seat seat : reservation.getSeats()){
            Seat loaded = seatService.getById(seat.getId());
            if ( loaded.getStatus() != SeatStatus.AVAILABLE){
                throw new IllegalArgumentException(
                    "이미 예약된 좌석이 포함되어 있습니다: "+
                    loaded.getSeatRow() + loaded.getSeatNumber()
                );
            }
            loaded.setStatus(SeatStatus.RESERVED);
            seatService.createOrUpdateSeat(loaded);
        }
        return dao.save(reservation);
    }


    // 기존 예약 수정
    // 고객 정보와 예약 좌석 목록, 예약 시간을 업데이트
    @Transactional
    public Reservation update(Long id, Reservation updated){
        Reservation existing = dao.findById(id);
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerCategory(updated.getCustomerCategory());
        existing.setReservedAt(updated.getReservedAt());
        existing.setSeats(updated.getSeats());
        return dao.save(existing);
    }

    // 예약 삭제
    @Transactional
    public void delete(Long id){
        dao.deleteById(id);
    }

    // 고객 이름으로 예약 검색
    @Transactional(readOnly = true)
    public List<Reservation> findByCustomerName(String name){
        return dao.findCustomerName(name);
    }

    // 특정 좌석 ID가 포함된 예약 검색
    @Transactional(readOnly = true)
    public List<Reservation> findBySeatId(Long seatId){
        return dao.findBySeatId(seatId);
    }

    // 예약 시간 범위 내 예약 검색
    @Transactional(readOnly = true)
        public List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to){
            return dao.findByReservedAtBetween(from, to);
        }
    
    

}
