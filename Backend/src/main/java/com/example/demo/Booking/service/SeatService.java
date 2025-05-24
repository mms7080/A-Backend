package com.example.demo.Booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.SeatDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.repository.SeatRepository;

// 특정 상영시간의 좌석 조회, 좌석 상태 변경(임시 선택, 확정, 해제) 등의 기능을 제공
@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository){
        this.seatRepository = seatRepository;
    }

    // 특정 상영시간(showtimeId)에 해당하는 모든 좌석 정보를 SeatDto 형태로 조회
    @Transactional(readOnly = true)
    public List<SeatDto> getSeatsForShowtime(Long showtimeId) { //
        
        return seatRepository.findByShowtimeId(showtimeId).stream() //
                .map(SeatDto::fromEntity) 
                .collect(Collectors.toList()); 
    }

    // 주어진 좌석 ID 목록(seatIds)에 해당하는 좌석들의 상태를 'HELD'(임시 선택)로 변경
    @Transactional 
    public boolean holdSeats(List<Long> seatIds, Long showtimeId) {
        
        List<Seat> seatsToHold = seatRepository.findByIdIn(seatIds); //
        
        if (seatsToHold.size() != seatIds.size()) {
            throw new RuntimeException("일부 좌석을 찾을 수 없습니다. (요청된 ID 개수: " + seatIds.size() + ", 조회된 좌석 개수: " + seatsToHold.size() + ")");
        }

        for (Seat seat : seatsToHold) { //
            // 각 좌석에 대해 다음 조건들을 검증
            // 1. 좌석이 올바른 상영시간(showtimeId)에 속하는가? (showtimeId를 통해 상영시간 일치 여부 확인)
            // 2. 좌석의 현재 상태가 'AVAILABLE'(예약 가능)인가?
            if (seat.getShowtime().getId().equals(showtimeId) && seat.getStatus() == SeatStatus.AVAILABLE) { //
                seat.setStatus(SeatStatus.HELD); 
            } else {
                throw new RuntimeException("좌석 " + seat.getSeatRow() + seat.getSeatNumber() + "(ID:" + seat.getId() + ")는 현재 선택할 수 없는 상태입니다. (현재 상태: " + seat.getStatus() + ", 요청된 상영시간 ID: " + showtimeId + ", 좌석의 상영시간 ID: " + seat.getShowtime().getId() + ")");
            }
        }
        seatRepository.saveAll(seatsToHold); 
        return true; 
    }


    // 주어진 좌석 ID 목록(seatIds)에 해당하는 좌석들의 상태를 'RESERVED'(예약 완료)로 변경
     @Transactional
    public void confirmSeats(List<Long> seatIds, Long showtimeId) {
        List<Seat> seatsToConfirm = seatRepository.findByIdIn(seatIds); //
        if (seatsToConfirm.size() != seatIds.size()) { // 요청된 ID 수와 실제 조회된 좌석 수가 다르면 예외 발생
            throw new RuntimeException("예약 확정 처리 중 일부 좌석을 찾을 수 없습니다.");
        }
        for (Seat seat : seatsToConfirm) { //
            // 각 좌석에 대해 다음 조건들을 검증
            // 1. 좌석이 올바른 상영시간(showtimeId)에 속하는가?
            // 2. 좌석의 현재 상태가 'HELD'(임시 선택)인가?
            if (seat.getShowtime().getId().equals(showtimeId) && seat.getStatus() == SeatStatus.HELD) { //
                seat.setStatus(SeatStatus.RESERVED);
            } else {
                throw new RuntimeException("좌석 " + seat.getSeatRow() + seat.getSeatNumber() + "(ID:" + seat.getId() + ")는 예약 확정할 수 없는 상태입니다. (현재 상태: " + seat.getStatus() + ", 기대 상태: HELD)");
            }
        }
        seatRepository.saveAll(seatsToConfirm); 
    }

    // 주어진 좌석 ID 목록(seatIds)에 해당하는 'HELD' 상태의 좌석들을 다시 'AVAILABLE'(예약 가능)로 되돌림
    @Transactional
    public void releaseSeats(List<Long> seatIds, Long showtimeId) {
        List<Seat> seatsToRelease = seatRepository.findByIdIn(seatIds); 
        
        if (seatsToRelease.size() != seatIds.size()) {
            // 실제 운영 환경에서는 이 상황에 대한 로깅이 중요할 수 있음
            System.err.println("좌석 해제(release) 처리 중 일부 좌석 ID를 찾을 수 없거나 유효하지 않습니다. 요청된 ID 개수: " + seatIds.size() + ", 실제 처리 대상 좌석 개수: " + seatsToRelease.size());
        }

        for (Seat seat : seatsToRelease) { //
           
            if (seat.getShowtime().getId().equals(showtimeId) && seat.getStatus() == SeatStatus.HELD) { //
                seat.setStatus(SeatStatus.AVAILABLE); 
            }
            // 'RESERVED' 상태의 좌석은 이 메서드에서 변경하지 않음 (별도의 '예매 취소' 로직 필요)
            // 'AVAILABLE' 상태의 좌석도 변경할 필요가 없음
        }
        if (!seatsToRelease.isEmpty()) { 
            seatRepository.saveAll(seatsToRelease); 
        }
    }

    // 특정 좌석 ID(seatId)에 해당하는 좌석(Seat) 엔티티를 조회
    @Transactional(readOnly = true)
    public Seat getSeatById(Long seatId) { 
        return seatRepository.findById(seatId) 
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
    }
}
