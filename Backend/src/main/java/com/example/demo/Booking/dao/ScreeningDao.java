package com.example.demo.Booking.dao;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.Booking.entity.Screening;
import com.example.demo.Booking.repository.ScreeningRepository;

@Repository  // 데이터 접근 전용 빈임을 명시합니다.
public class ScreeningDao {

    private final ScreeningRepository repo;

    public ScreeningDao(ScreeningRepository repo) {
        this.repo = repo;
    }

    /** 모든 상영회차 조회 */
    public List<Screening> findAll() {
        return repo.findAll();
    }

    /** 특정 상영회차 ID로 조회 (없으면 예외) */
    public Screening findById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상영회차 ID: " + id));
    }

    /** 새로운 상영회차 저장 또는 기존 업데이트 */
    public Screening save(Screening screening) {
        return repo.save(screening);
    }

    // 여러 상영회차를 한번에 저장
    public List<Screening> saveAll(List<Screening> screenings){
        return repo.saveAll(screenings);
    }

    /** 특정 상영관(theaterName)에 해당하는 회차만 조회 */
    public List<Screening> findByTheaterName(String theaterName) {
        return repo.findByTheaterName(theaterName);
    }

    /** 영화 제목에 키워드(keyword)가 포함된 회차 조회 (페이징 지원) */
    public List<Screening> findByMovieTitleContaining(String keyword) {
        // Pageable.unpaged()를 넘겨 전체 결과를 가져옵니다.
        return repo.findByMovieTitleContaining(keyword, Pageable.unpaged())
                   .getContent();
    }

    /** 특정 기간(from~to) 내의 상영회차 조회 */
    public List<Screening> findByStartTimeBetween(LocalDateTime from, LocalDateTime to) {
        return repo.findByStartTimeBetween(from, to);
    }

    /** 현재 시각(now) 이후에 시작하는(예약 가능한) 회차만 조회 */
    public List<Screening> findUpcoming(LocalDateTime now) {
        return repo.findByStartTimeAfter(now);
    }

    /** 특정 상영회차 삭제 */
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}