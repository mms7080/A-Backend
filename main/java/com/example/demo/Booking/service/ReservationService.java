package com.example.demo.Booking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 모든 예약 목록을 조회합니다.
     * 읽기 전용 트랜잭션으로 설정하여 데이터 변경 없이 조회만 수행함을 명시합니다.
     * @return 모든 예약 엔티티 목록
     */
    @Transactional(readOnly = true)
    public List<Reservation> findAll(){ // 추가된 findAll() 메서드
        return dao.findAll();
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

        // 요청된 좌석 ID 목록에서 중복을 제거하기 위해 Set을 사용
        Set<Long> requestedSeatIds = reservation.getSeats().stream()
                                                .map(Seat::getId)
                                                .collect(Collectors.toSet());


        List<Seat> reservedSeats = new ArrayList<>(); // 실제로 예약될 좌석 목록
        // 요청된 각 좌석 ID에 대해 처리
        for(Long seatId : requestedSeatIds){
            Seat loaded = seatService.getById(seatId); // 좌석 ID로 좌석 정보 조회
            if ( loaded.getStatus() != SeatStatus.AVAILABLE){
                throw new IllegalArgumentException(
                    "이미 예약된 좌석이 포함되어 있습니다: "+
                    loaded.getSeatRow() + loaded.getSeatNumber()
                );
            }
            loaded.setStatus(SeatStatus.RESERVED); // 좌석 상태를 결제완료
            seatService.createOrUpdateSeat(loaded); // 변경된 좌석 상태를 데이터베이스에 반영
            reservedSeats.add(loaded); // 예약에 포함될 좌석 목록에 추가
        }
        reservation.setSeats(reservedSeats); // 최종적으로 예약에 확정된 좌석 목록을 설정
        return dao.save(reservation); // 예약 정보 저장
    }


    // 기존 예약 수정
    // 고객 정보와 예약 좌석 목록, 예약 시간을 업데이트
    @Transactional
    public Reservation update(Long id, Reservation updated){
        Reservation existing = dao.findById(id); 

        // 기존 예약에 포함된 좌석들의 상태를 예약가능으로 변경하고 유효성 검사
        List<Seat> newSeats = new ArrayList<>(); // 새로 예약될 좌석 목록
        if(updated.getSeats() != null){
            // 요청된 새로운 좌석 ID 목록에서 중복 제거
            Set<Long> requestedSeatIds = updated.getSeats().stream()
                                                .map(Seat::getId)
                                                .collect(Collectors.toSet());
            for(Long seatId : requestedSeatIds){
                Seat loaded = seatService.getById(seatId); // 새로운 좌석 정보 조회
                if(loaded.getStatus() != SeatStatus.AVAILABLE && !existing.getSeats().stream()
                    .anyMatch(s -> s.getId().equals(loaded.getId()))) {
                        throw new IllegalArgumentException(
                            "이미 예약된 좌석이 포함되어 있습니다.:" + 
                            loaded.getSeatRow() + loaded.getSeatNumber()
                        );
                    }
                    loaded.setStatus(SeatStatus.RESERVED); // 좌석 상태를 결제완료로 변경
                    seatService.createOrUpdateSeat(loaded); // 변경된 좌석 상태를 데이터베이스에 반영
                    newSeats.add(loaded); // 새로 예약될 좌석 목록에 추가
            }
        }
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerCategory(updated.getCustomerCategory());
        existing.setReservedAt(updated.getReservedAt());
        existing.setSeats(newSeats); // 새로운 좌석 목록으로 설정
        return dao.save(existing);
    }

    // 예약 삭제
    @Transactional
    public void delete(Long id){
        Reservation existing =  dao.findById(id); // 삭제할 예약 정보를 조회

        // 예약에 포함된 좌석들의 상태를 예약으로 되돌림
        for(Seat seat : existing.getSeats()){
            Seat loadedSeat = seatService.getById(seat.getId());
            loadedSeat.setStatus(SeatStatus.AVAILABLE); // 좌석 상태를 예약 가능으로 변경
            seatService.createOrUpdateSeat(loadedSeat); // 변경된 좌석 상태를 데이터베이스에 변경
        }
        dao.deleteById(id); // 예약 정보 삭제
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
