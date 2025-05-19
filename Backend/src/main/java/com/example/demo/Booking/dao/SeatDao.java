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

	// 모든 좌석 조회
	public List<Seat> findAll(){
		return repo.findAll();
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

	// 연속된 사용 가능 좌석 조회
    public List<Seat> findContiguous(Long screeningId, int count) {
        // 실제 로직에서 count 파라미터 활용해 슬라이딩 윈도우 구현 필요
        return repo.findContiguousByScreeningIdAndStatus(screeningId, SeatStatus.AVAILABLE);
    }

	// 사용 가능한 좌석 개수 계산
    public long countAvailable(Long screeningId) {
        return repo.countByScreeningIdAndStatus(screeningId, SeatStatus.AVAILABLE);
    }
}
