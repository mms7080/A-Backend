package com.example.demo.Booking.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.Booking.dao.ScreeningDao;
import com.example.demo.Booking.entity.Screening;

@Service
// Screening 관련 서비스 로직을 처리하는 Service
public class ScreeningService {
    
    private final ScreeningDao screeningDao; // DAO 주입
    

    public ScreeningService(ScreeningDao screeningDao){
        this.screeningDao = screeningDao;
        
    }

    // 모든 상영회차 조회(읽지 전용)
    @Transactional(readOnly = true)
    public List<Screening> getAllScreenings(){
        return screeningDao.findAll();
    }

    // 단일 상영회차 조회(ID로 조회)
    // 값이 없으면 IllegalArgumentException 예외 발생
    @Transactional(readOnly = true)
    public Screening getScreeningById(Long id){
        return screeningDao.findById(id);
    }

    // 새로운 상영회차 등록
    // movieTile: null / 빈 문자열 금지
    // startTime: 현재 시각 이후여야 함
    @Transactional
    public Screening createScreening(Screening screening){
        validateScreening(screening); // 입력 검증 로직 실행
        return screeningDao.save(screening); // DAO를 통해 저장
    }

    /*기존 상영회차 수정
     * 존재 여부 확인
     * 검증 로직 재사용
     * 변경 후 저장
     */
    @Transactional
    public Screening updateScreening(Long id, Screening updated){
        // 1. 존재 여부 확인 (없으면 예외처리)
        Screening existing = screeningDao.findById(id);

        // 2. 검증 로직 적용
        validateScreening(updated);

        // 3. 필드 업데이트
        existing.setMovieTitle(updated.getMovieTitle());
        existing.setTheaterName(updated.getTheaterName());
        existing.setStartTime(updated.getStartTime());

        // 4. 변경된 엔터티 저장
        return screeningDao.save(existing);
    }

    // 상영회차 삭제
    @Transactional
    public void deleteScreening(Long id){
        screeningDao.deleteById(id);
    }

    // Screening 엔티티에 대한 공통 검증 로직
    private void validateScreening(Screening screening){
        // 1. 영화제목 검증: null, 빈 문자열일때 예외처리
        if(!StringUtils.hasText(screening.getMovieTitle())){
            throw new IllegalArgumentException("영화 제목은 반드시 입력해야 합니다.");
        }

        // 2. 상영 시간 검증 : 현재 시각(Clock 기준 )이전 일때 예외처리
        LocalDateTime now = LocalDateTime.now();
        if(screening.getStartTime() == null || screening.getStartTime().isBefore(now)){
            throw new IllegalArgumentException("상영 시작 시간은 현재 시간 이후여야 합니다.");
        }
    }
}
