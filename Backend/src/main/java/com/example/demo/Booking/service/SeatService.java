package com.example.demo.Booking.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    
    // 단일 좌석 조회(ID로 조회)
    @Transactional(readOnly = true)
    public Seat getById(Long id){
        return seatDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 좌석 ID: " + id));
        
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

	// 특정 상영회차에서 지정된 개수(count)만큼 연속된 사용 가능한 좌석을 찾음
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
	public List<Seat> findContiguousSeats(Long screeningId, int count){
		// 1. 해당 상영회차의 모든 사용 가능한 좌석을 가져와서 행(row)과 번호(number) 순으로 정렬합니다.
		List<Seat> availableSeats = seatDao.findByScreeningId(screeningId).stream()
				.filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE) // 사용 가능한 좌석만 필터링
				.sorted(Comparator
						.comparing(Seat::getSeatRow) // 행(row) 기준으로 먼저 정렬 (예: "A", "B")
						.thenComparing(Seat::getSeatNumber)) // 그 다음 좌석 번호(number) 기준으로 정렬 (예: 1, 2)
				.collect(Collectors.toList());
        // 2. 슬라이딩 윈도우 방식으로 연속된 좌석 그룹을 탐색
        // 'i'는 현재 윈도우의 시작 인덱스를 나타냄
        // 'availableSeats.size() - count'는 윈도우가 끝까지 이동할 수 있는 최대 시작 인덱스
        for (int i =0; i <=availableSeats.size() - count; i++){
            boolean isContiguous = true; 
            List<Seat> currenContiguousSeats = new ArrayList<>();
            currenContiguousSeats.add(availableSeats.get(i)); // 첫번째 좌석 추가

            // 나머지 좌석들을 확인하며 연속성을 검사
            for(int j =1; j<count; j++){
                Seat prevSeat = availableSeats.get(i + j -1); // 이전 좌석
                Seat currentSeat = availableSeats.get(i + j);  // 현재 좌석

                // 같은 행인지 확인, 행이 다르면 연속적이지 않음
                if (!prevSeat.getSeatRow().equals(currentSeat.getSeatRow())){
                    isContiguous = false;
                    break;
                }
                // 좌석 번호가 이전 좌석 번호 + 1인지 확인, 연속적이지 않으면 중단
                if(currentSeat.getSeatNumber() != prevSeat.getSeatNumber() +1) {
                    isContiguous = false;
                    break;
                }
                currenContiguousSeats.add(currentSeat); // 연속적이면 현재 좌석을 리스트에 추가
            }
            // 3. 만약 현재 윈도우의 모든 좌석이 연속적이라면, 해당 좌석 리스트를 반환
            if(isContiguous){
                return currenContiguousSeats;
            }
        }
        // 4. 모든 윈도우를 탐색했지만 연속된 좌석을 찾지 못했으면 빈 리스트를 반환
            return List.of();
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
     * screeningId에 속한 샘플 좌석을 행 A~L, 번호 1~9번 열로 자동 생성(seed)
     * 이미 좌석이 존재하면 그대로 반환(중복 시드 방지)
     * 일괄 저장(saveAll) 처리로 성능 최적화
     */
    
    @Transactional
    public List<Seat> seedSeats(Long screeningId) {
        // 1) 상영회차가 유효한지 확인
        Screening screening = screeningDao.findById(screeningId);

        // 2) 이미 시드된 좌석이 있으면 재사용
        List<Seat> exisiting = seatDao.findByScreeningId(screeningId);
        if(!exisiting.isEmpty()){
            return exisiting;
        }
            

        // 3) A~L(5행), 각 행당 1~9번 좌석 생성
        List<Seat> created = new ArrayList<>();
        for(char row = 'A'; row <='L'; row++){
            for(int num = 1; num <=9; num++){
                Seat seat = new Seat();
                seat.setSeatRow(String.valueOf(row)); // 행 설정(문자)
                seat.setSeatNumber(num); // 열 설정(숫자)
                seat.setStatus(SeatStatus.AVAILABLE); // 초기 상태: 사용 가능
                seat.setScreening(screening); // 상영회차 연관
                created.add(seat);
            }
        }
        // 4) 일괄 저장 후 반환
        return seatDao.saveAll(created);
    }
}
