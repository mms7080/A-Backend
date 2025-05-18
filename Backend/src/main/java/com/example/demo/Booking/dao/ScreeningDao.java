package com.example.demo.Booking.dao;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.Booking.entity.Screening;
import com.example.demo.Booking.repository.ScreeningRepository;

@Component
public class ScreeningDao {
    
    public final ScreeningRepository repo;

    public ScreeningDao(ScreeningRepository repo){
        this.repo = repo;
    }

    // 모든 상영회차 조회
    public List<Screening> findAll(){
        return repo.findAll();
    }

    // 새로운 상영회차 저장 또는 업데이트
    public Screening save(Screening screening){
        return repo.save(screening);
    }

    // 특정 상영회차 ID로 조회
    public Screening findById(Long id){
        return repo.findById(id)
                    .orElseThrow(() -> {
                        return new IllegalArgumentException("유효하지 않은 상영회차 ID:" + id);
                    });
    }

    // 특정 극장의 상영회차만 조회
    public List<Screening> findByTheaterName(String theaterName){
        return repo.findByTheaterName(theaterName);
    }

    // 영화 제목에 키워드가 포함된 상영회차 조회 (페이징 없이 전체)
    public List<Screening> fingByMovieTitleContaining(String keyword){
        return repo.findByMovieTitleContaining(keyword, null).getContent();
    }

    // 앞으로 시작하는(현재 시간 이후) 상영회차만 조회
    public List<Screening> findUpcoming(LocalDateTime now){
        return repo.findByStartTimeAfter(now);
    }

    // 상영회차 삭제
    public void deleteById(Long id){
        repo.deleteById(id);
    }
}
