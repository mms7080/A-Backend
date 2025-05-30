package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.response.SeatDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    @Transactional
    public List<SeatDto> getSeatsByShowtime(Long showtimeId) {
        // 1. 상영 시간표 엔티티를 조회합니다. 없으면 예외 발생.
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상영 시간표를 찾을 수 없습니다: " + showtimeId));

        // 2. 해당 상영 시간표의 좌석이 이미 존재하는지 확인
        List<Seat> seats = seatRepository.findAllByShowtimeId(showtimeId);

        // 3. 좌석이 이미 존재하면 그대로 DTO로 변환하여 반
        if (!seats.isEmpty()) {
            return seats.stream()
                    .map(SeatDto::fromEntity)
                    .collect(Collectors.toList());
        }

        // 4. 좌석이 존재하지 않으면 새로 생성
        List<Seat> newSeats = new ArrayList<>();
        for (char row = 'A'; row <= 'I'; row++) { // A열부터 I열까지
            for (int number = 1; number <= 12; number++) { // 1번부터 12번까지
                Seat newSeat = Seat.builder()
                        .showtime(showtime)
                        .seatRow(String.valueOf(row))
                        .seatNumber(number)
                        .status(SeatStatus.AVAILABLE) // 모든 좌석은 '예매 가능' 상태로 시작
                        .build();
                newSeats.add(newSeat);
            }
        }

        // 5. 새로 생성된 좌석들을 데이터베이스에 한 번에 저장합니다.
        List<Seat> savedSeats = seatRepository.saveAll(newSeats);

        // 6. 저장된 좌석들을 DTO로 변환하여 반환합니다.
        return savedSeats.stream()
                .map(SeatDto::fromEntity)
                .collect(Collectors.toList());
    }
}