package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.SeatDto;
import com.example.demo.Booking.service.SeatService;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/booking/seats")
public class SeatController {

	private final SeatService seatService;

	public SeatController(SeatService seatService){
		this.seatService = seatService;
	}

	// get /booking/seats?screeingId=}{screening}
	// 특정 상영회차(screening)에 속한 좌석 목록 조회
	// 조회한 Seat 엔티티를 SeatDto로 변환해서 반환
	
	
	
}
