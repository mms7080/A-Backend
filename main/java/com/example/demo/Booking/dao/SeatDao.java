package com.example.demo.Booking.dao;

import java.util.List;
import java.util.Optional;

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

	// 모든 좌석 조회
	public List<Seat> findAll(){
		return repo.findAll();
	}

    // 단일 좌석 조회
    public Optional<Seat> findById(Long id){
        return repo.findById(id);
    }

    /**
     * 특정 ID로 단일 좌석 조회 / 없으면 예외 던짐
     */
    public Seat getById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 좌석 ID: " + id));
    }

	// 좌석 저장
    public Seat save(Seat seat) {
        return repo.save(seat);
    }

	// 좌석 삭제
	public void deleteById(Long id){
		 repo.deleteById(id);
	}

	// 상영회차 ID로 해당 상영회차의 모든 좌석 조회
    public List<Seat> findByScreeningId(Long screeningId) {
        return repo.findByScreeningId(screeningId);
    }


	// 사용 가능한 좌석 개수 계산
    public long countAvailable(Long screeningId) {
        return repo.countByScreeningIdAndStatus(screeningId, SeatStatus.AVAILABLE);
    }

    /**
     * 여러 좌석을 한 번에 저장
     */
    public List<Seat> saveAll(List<Seat> seats) {
        return repo.saveAll(seats);
    }
}
