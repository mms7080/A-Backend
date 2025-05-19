package com.example.demo.Booking.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.repository.SeatRepository;

@Repository
public class SeatDao {
	private final SeatRepository repo;

    public SeatDao(SeatRepository repo) {
        this.repo = repo;
    }

    public Seat save(Seat seat) {
        return repo.save(seat);
    }

    public List<Seat> findByScreeningId(Long screeningId) {
        return repo.findByScreeningId(screeningId);
    }

    public List<Seat> findContiguousAvailableSeats(Long screeningId, int count) {
        // 실제 로직에서 count 파라미터 활용해 슬라이딩 윈도우 구현 필요
        return repo.findContiguousByScreeningIdAndStatus(screeningId, SeatStatus.AVAILABLE);
    }

    public long countAvailableSeats(Long screeningId) {
        return repo.countByScreeningIdAndStatus(screeningId, SeatStatus.AVAILABLE);
    }
}
