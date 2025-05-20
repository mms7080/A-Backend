package com.example.demo.Booking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.Booking.dao.ScreeningDao;
import com.example.demo.Booking.dao.SeatDao;
import com.example.demo.Booking.entity.Screening;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;


@Service
public class SeatService {
	private final SeatDao seatDao;
	private final ScreeningDao screeningDao;

    public SeatService(SeatDao seatDao, ScreeningDao screeningDao) {
        this.seatDao = seatDao;
		this.screeningDao = screeningDao;
    }

	// 모든 좌석 조회
	@Transactional(readOnly = true)
	public List<Seat> getAllSeats(){
		return seatDao.findAll();
	}

	// 특정 상영회차에 속한 좌석 목록 조회
	@Transactional(readOnly = true)
	public List<Seat> getSeatsByScreening(Long screeningId){
		return seatDao.findByScreeningId(screeningId);
	}

	// 특정 상영회차의 사용 가능한 좌석 수 반환
	@Transactional(readOnly = true)
	public long countAvailableSeats(Long screeingId){
		return seatDao.countAvailable(screeingId);
	}

	// 연속된 사용 가능한 좌석 조회
	@Transactional(readOnly = true)
	public List<Seat> findContiguousSeats(Long screeningId, int count){
		return seatDao.findContiguous(screeningId, count);
	}

	// 좌석 생성
	@Transactional
	public Seat createOrUpdateSeat(Seat seat){
		validateSeat(seat);
		return seatDao.save(seat);
	}

	// 저장 전 검증 메서드
	private void validateSeat(Seat seat) {
        // 1) row(행)와 number(번호) 필수 체크
        if (!StringUtils.hasText(seat.getSeatRow())) {
            throw new IllegalArgumentException("좌석 행(row)은 반드시 입력해야 합니다.");
        }
        if (seat.getSeatNumber() == null || seat.getSeatNumber() < 1) {
            throw new IllegalArgumentException("좌석 번호는 1 이상의 값이어야 합니다.");
        }

        // 2) status(상태) 필수 체크
        if (seat.getStatus() == null) {
            throw new IllegalArgumentException("좌석 상태(status)는 반드시 입력해야 합니다.");
        }

        // 3) 연관된 Screening(상영회차) 유효성 검사
        if (seat.getScreening() == null || seat.getScreening().getId() == null) {
            throw new IllegalArgumentException("유효한 상영회차(screening)가 설정되어야 합니다.");
        }
        // (DAO를 통해 실제 존재 여부 확인)
        screeningDao.findById(seat.getScreening().getId());
    }

	// 좌석 삭제
	@Transactional
	public void deleteSeat(Long id){
		seatDao.deleteById(id);
	}
    
	/**
     * screeningId에 속한 샘플 좌석을 행 A~E, 번호 1~10 으로 생성해서 저장한 뒤,
     * 생성된 좌석 목록을 반환합니다.
     */
    @Transactional
    public List<Seat> seedSeats(Long screeningId) {
        // 1) 상영회차가 유효한지 확인
        Screening screening = screeningDao.findById(screeningId);

        // 2) A~E(5행), 각 행당 1~10번 좌석 생성
        List<Seat> created = new ArrayList<>();
        for (int r = 1; r <= 5; r++) {
            char rowChar = (char)('A' + r - 1);
            for (int num = 1; num <= 10; num++) {
                Seat seat = new Seat();
                seat.setSeatRow(String.valueOf(rowChar));
                seat.setSeatNumber(num);
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setScreening(screening);
                created.add(seatDao.save(seat));
            }
        }
        return created;
    }
}
