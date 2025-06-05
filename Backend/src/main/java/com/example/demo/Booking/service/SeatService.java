package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.response.SeatDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    @Transactional
    public List<SeatDto> getSeatsByShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상영 시간표를 찾을 수 없습니다: " + showtimeId));

        // 해당 상영 시간표의 좌석이 이미 존재하는지 확인
        List<Seat> seats = seatRepository.findAllByShowtimeId(showtimeId);

        // 좌석이 없으면 새로 생성 (API 첫 호출 시)
        if (seats.isEmpty()) {
            log.info("No seats found for showtimeId: {}. Generating new seats A1-I12...", showtimeId);
            List<Seat> newSeats = new ArrayList<>();
            for (char row = 'A'; row <= 'I'; row++) { 
                for (int number = 1; number <= 12; number++) {
                    Seat newSeat = Seat.builder()
                            .showtime(showtime)
                            .seatRow(String.valueOf(row))
                            .seatNumber(number)
                            .status(SeatStatus.AVAILABLE) 
                            .build();
                    newSeats.add(newSeat);
                }
            }
            seats = seatRepository.saveAll(newSeats);
            log.info("{} seats created and saved for showtimeId: {}", seats.size(), showtimeId);
        } else {
            log.info("Found {} existing seats for showtimeId: {}", seats.size(), showtimeId);
        }

        // --- 모든 Showtime에 대해 A3, A4, A9, A10 좌석을 UNAVAILABLE (장애인석) 상태로 설정 ---
        // 이 로직은 실제 운영 환경에서는 상영관의 고유한 좌석 배치도 정보를 DB에서 읽어와 처리해야 합니다.
        // 현재는 테스트를 위해 모든 상영관의 레이아웃이 동일하다고 가정하고 하드코딩합니다.
        
        List<Seat> seatsToUpdate = new ArrayList<>();

        for (Seat seat : seats) {
            String fullSeatName = seat.getFullSeatName();
            
            // 지정된 장애인석 위치인지 확인
            boolean isPreferentialSeat = 
                "A3".equals(fullSeatName) || "A4".equals(fullSeatName) || 
                "A9".equals(fullSeatName) || "A10".equals(fullSeatName);

            if (isPreferentialSeat) {
                // 이미 UNAVAILABLE 상태가 아니라면 상태 변경
                if (seat.getStatus() != SeatStatus.UNAVAILABLE) { 
                    seat.setStatus(SeatStatus.UNAVAILABLE);
                    seatsToUpdate.add(seat);
                    log.info("Seat {} in showtimeId: {} is being set to UNAVAILABLE (Preferential Seat).", fullSeatName, showtimeId);
                }
            }
        }

        // 상태가 변경된 좌석이 있다면 DB에 저장
        if (!seatsToUpdate.isEmpty()) {
            seatRepository.saveAll(seatsToUpdate);
            log.info("{} seats had their status updated to UNAVAILABLE in DB for showtimeId: {}", seatsToUpdate.size(), showtimeId);
        }
        // --- 테스트용 코드 종료 ---

        // 최종 좌석 목록을 DTO로 변환하여 반환
        return seats.stream()
                .map(SeatDto::fromEntity)
                .collect(Collectors.toList());
    }
}