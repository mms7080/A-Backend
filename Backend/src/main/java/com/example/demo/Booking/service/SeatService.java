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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    @Transactional
    public List<SeatDto> setSeatsByShowtime(Long showtimeId, String seatName, SeatStatus status) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상영 시간표를 찾을 수 없습니다: " + showtimeId));

        // 해당 상영 시간표의 좌석이 이미 존재하는지 확인
        List<Seat> seats = seatRepository.findAllByShowtimeId(showtimeId);
        if(seats.isEmpty()) throw new ResourceNotFoundException("해당 ID의 상영 시간표에 좌석이 없습니다: " + showtimeId);

        seats = seats.stream().filter(seat->seat.getFullSeatName().equalsIgnoreCase(seatName)).map(seat->{seat.setStatus(status); return seat;}).toList();
        if(seats.isEmpty()) throw new ResourceNotFoundException("해당 좌표의 좌석이 존재하지 않습니다.: " + seatName);
        seatRepository.saveAll(seats);
        
        // 최종 좌석 목록을 DTO로 변환하여 반환
        return seats.stream()
                .map(SeatDto::fromEntity)
                .collect(Collectors.toList());
    }
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

            Set<String> isPreferentialSeat = Set.of("A3", "A4", "A9", "A10");
            for (char row = 'A'; row <= 'I'; row++) { 
                for (int number = 1; number <= 12; number++) {

                    String fullSeatName = String.valueOf(row) + number;

                    SeatStatus initialStatus = isPreferentialSeat.contains(fullSeatName)
                        ? SeatStatus.UNAVAILABLE : SeatStatus.AVAILABLE;
                    Seat newSeat = Seat.builder()
                            .showtime(showtime)
                            .seatRow(String.valueOf(row))
                            .seatNumber(number)
                            .status(initialStatus)
                            .build();
                    newSeats.add(newSeat);
                }
            }
            seats = seatRepository.saveAll(newSeats);
            log.info("{} seats created and saved for showtimeId: {}", seats.size(), showtimeId);
        } else {
            log.info("Found {} existing seats for showtimeId: {}", seats.size(), showtimeId);
        }


        // 최종 좌석 목록을 DTO로 변환하여 반환
        return seats.stream()
                .map(SeatDto::fromEntity)
                .collect(Collectors.toList());
    }
}