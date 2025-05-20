package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.SeatDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.service.SeatService;

import java.security.cert.CollectionCertStoreParameters;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/booking/seats")
public class SeatController {

	private final SeatService seatService;

	public SeatController(SeatService seatService){
		this.seatService = seatService;
	}

	// 1. /booking/seats?screeingId=}{screening}
	// 특정 상영회차(screening)에 속한 좌석 목록 조회
	// 조회한 Seat 엔티티를 SeatDto로 변환해서 반환
	
	@GetMapping
	public List<SeatDto> getSeats(@RequestParam Long screeningId){
		return seatService.getSeatsByScreening(screeningId).stream()
			.map(SeatDto::fromEntity)
			.collect(Collectors.toList());
	}
	// 2. /booking/seats/available/count?screeningId={screeningId}
	// 특정 상영회차의 사용 가능한 좌석 수만 조회

	@GetMapping("/available/count")
	public Long countAailable(@RequestParam Long screeningId){
		return seatService.countAvailableSeats(screeningId);
	}

	// 3. booking/seats/contiguous?screeningId={screeningId}&count={count}
	// 연속된 지정 개수(count)만큼 사용 가능한 좌석 목록 조회
	// SeatDto 리스트로 변환해서 반환

	@GetMapping("/contiguous")
	public List<SeatDto> getContiguous(
		@RequestParam Long screeningId,
		@RequestParam int count
		) {
		return seatService.findContiguousSeats(screeningId, count).stream()
			.map(SeatDto::fromEntity)
			.collect(Collectors.toList());
	}
	// 4. post /booking/seats
	// 요청 본문(JSON)으로 들어온 SeatDto를 엔티티로 변환하여 저장

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SeatDto creatSeat(@RequestBody SeatDto dto){
		Seat seat = dto.toEntity();
		Seat created = seatService.createOrUpdateSeat(seat);
		return SeatDto.fromEntity(created);
	}

	// 5. Delete / booking/seats/{id}
	// 경로 변수(id)에 해당하는 좌석 삭제
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSeat(@PathVariable Long id){
		seatService.deleteSeat(id);
	}


	/**
     * GET /booking/seats/seed/{screeningId}
     * 지정된 상영회차(screeningId)에 대한 샘플 좌석을 생성하고,
     * 생성된 좌석 목록을 DTO로 반환합니다.
     */
	@GetMapping("/seed/{screeningId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SeatDto> seedSeats(@PathVariable Long screeningId) {
        return seatService.seedSeats(screeningId)
                          .stream()
                          .map(SeatDto::fromEntity)
                          .toList();
    }
	

	
	
	
}
