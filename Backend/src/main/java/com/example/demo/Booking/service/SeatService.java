package com.example.demo.Booking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dao.SeatDao;
import com.example.demo.Booking.entity.Seat;

@Service
public class SeatService {
	private final SeatDao seatDao;

    public SeatService(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    /** 좌석 생성 */
    @Transactional
    public Seat createSeat(Seat seat) {
        validateSeat(seat);
        return seatDao.save(seat);
    }

    /** 연속된 사용 가능한 좌석 조회 */
    @Transactional(readOnly = true)
    public List<Seat> findContiguousAvailableSeats(Long screeningId, int count) {
        return seatDao.findContiguousAvailableSeats(screeningId, count);
    }

    /** 사용 가능한 좌석 수 반환 */
    @Transactional(readOnly = true)
    public long countAvailableSeats(Long screeningId) {
        return seatDao.countAvailableSeats(screeningId);
    }

    /** 좌석 입력 검증 */
    private void validateSeat(Seat seat) {
        if (seat.getScreening() == null) {
            throw new IllegalArgumentException("좌석은 반드시 상영회차와 연관되어야 합니다.");
        }
        // row, number, status 등 추가 검증 가능
    }
}
