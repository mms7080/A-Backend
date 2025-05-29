package com.example.demo.Booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.response.SeatDto;
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

    
}
