package com.example.demo.Booking.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.Booking.dao.ScreeningDao;
import com.example.demo.Booking.entity.Screening;

@Service  // 스프링의 서비스 빈으로 등록
public class ScreeningService {

    private final ScreeningDao screeningDao;

    // 생성자 주입: DAO만 주입받도록 변경
    public ScreeningService(ScreeningDao screeningDao) {
        this.screeningDao = screeningDao;
    }

    /** 
     * 모든 상영회차 조회 
     * 읽기 전용 트랜잭션 설정(@Transactional(readOnly = true))
     */
    @Transactional(readOnly = true)
    public List<Screening> getAllScreenings() {
        return screeningDao.findAll();
    }

    /**
     * 단일 상영회차 조회 (ID 기준)
     * 존재하지 않으면 IllegalArgumentException 발생
     */
    @Transactional(readOnly = true)
    public Screening getScreeningById(Long id) {
        return screeningDao.findById(id);
    }

    /**
     * 새로운 상영회차 등록
     * 검증 후 DAO를 통해 저장
     */
    @Transactional
    public Screening createScreening(Screening screening) {
        validateScreening(screening);         // 입력값 검증
        return screeningDao.save(screening);  // 저장
    }

    /**
     * 기존 상영회차 수정
     * 1) 존재 여부 확인
     * 2) 검증 로직 재사용
     * 3) 필드 업데이트 후 저장
     */
    @Transactional
    public Screening updateScreening(Long id, Screening updated) {
        Screening existing = screeningDao.findById(id);  // 1. 조회(존재 확인)
        validateScreening(updated);                     // 2. 입력값 검증
        // 3. 필드 복사
        existing.setMovieTitle(updated.getMovieTitle());
        existing.setTheaterName(updated.getTheaterName());
        existing.setStartTime(updated.getStartTime());
        return screeningDao.save(existing);             // 4. 저장
    }

    /**
     * 상영회차 삭제
     */
    @Transactional
    public void deleteScreening(Long id) {
        screeningDao.deleteById(id);
    }

    /**
     * [검증 메서드]
     * - 영화 제목(movieTitle)이 null 또는 빈 문자열일 경우 예외
     * - 상영 시작 시간(startTime)이 현재 시간 이전일 경우 예외
     */
    private void validateScreening(Screening screening) {
        // 1. 영화 제목이 비어 있으면 예외
        if (!StringUtils.hasText(screening.getMovieTitle())) {
            throw new IllegalArgumentException("영화 제목은 반드시 입력해야 합니다.");
        }
        // 2. 상영 시작 시간이 현재 시간 이전이면 예외
        LocalDateTime now = LocalDateTime.now();  // Clock 없이 직접 현재 시각 조회
        if (screening.getStartTime() == null || screening.getStartTime().isBefore(now)) {
            throw new IllegalArgumentException("상영 시작 시간은 현재 시간 이후여야 합니다.");
        }
    }
}
