package com.example.demo.Notice;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.User.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository repo;
    private final UserRepository userRepository;

    
    public NoticeService(NoticeRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    // 공지 전체 조회 (최신순)
    public List<Notice> getAllNotices() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // 공지 생성
    public Notice create(Notice notice) {
        return repo.save(notice);
    }

    // ID로 공지 조회
    public Optional<Notice> getById(Long id) {
        return repo.findById(id);
    }

    // ID로 공지 삭제
    public boolean deleteById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    // 공지 수정
    public void update(Long id, NoticeDTO dto) {
        Notice notice = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 존재하지 않습니다: " + id));

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setWriter(dto.getWriter());

        repo.save(notice);
    }

    // 공지 상세 조회 + 조회수 증가
    public Notice getByIdWithViewIncrease(Long id) {
        return repo.findById(id).map(notice -> {
            notice.setViews(notice.getViews() + 1); 
            return repo.save(notice);
        }).orElse(null);
    }

    // 초기 더미 공지 등록
    @PostConstruct
    public void initDefaultNotices() {
        if (repo.count() == 0) {
            LocalDateTime now = LocalDateTime.now();

            List<Notice> notices = List.of(
                    createNotice("극장 리뉴얼 안내", "2025년 6월부터 필모라 강남점 리뉴얼 공사를 진행합니다. 관람에 불편을 드려 죄송합니다.", now),
                    createNotice("팝콘 무료 제공 이벤트", "6월 10일부터 16일까지 영화 관람 고객 전원에게 미니 팝콘을 무료로 드립니다.", now),
                    createNotice("조조 할인 변경 안내", "2025년 6월 1일부터 조조 할인 시간이 오전 11시까지로 조정됩니다.", now),
                    createNotice("4DX 상영관 오픈", "필모라 잠실점에 4DX 전용 상영관이 새롭게 오픈했습니다.", now),
                    createNotice("영화 예매 시스템 점검", "6월 5일(수) 오전 2시~4시까지 시스템 점검으로 예매 서비스가 중단됩니다.", now),
                    createNotice("멤버십 혜택 안내", "필모라 멤버십 등급별 혜택이 6월부터 일부 변경됩니다. 자세한 내용은 공지사항 참조.", now),
                    createNotice("분실물 보관 정책 변경", "2025년 6월부터 분실물 보관 기간이 30일에서 15일로 단축됩니다.", now),
                    createNotice("청소년 관람가 유의사항", "영화 관람 시 나이 확인을 위해 신분증 지참을 권장드립니다.", now),
                    createNotice("스낵코너 리뉴얼", "더욱 다양한 메뉴로 새단장한 스낵코너가 곧 오픈합니다. 많은 기대 부탁드립니다!", now),
                    createNotice("상영관 의자 교체 안내", "쾌적한 관람을 위해 일부 상영관 의자 교체 공사를 진행합니다.", now),
                    createNotice("팝콘 가격 인상 안내", "원재료 가격 상승으로 인해 팝콘 가격이 소폭 인상됩니다. 양해 부탁드립니다.", now),
                    createNotice("무대인사 일정 공지", "인기 배우들과 함께하는 무대인사 이벤트가 진행됩니다. 자세한 내용은 홈페이지를 참고해주세요.", now),
                    createNotice("시사회 초청 이벤트", "신작 영화 시사회에 고객 여러분을 초대합니다!", now),
                    createNotice("CGV 멤버십 혜택 변경 안내", "2025년 6월부터 멤버십 혜택 정책이 일부 변경됩니다.", now),
                    createNotice("주차장 임시 폐쇄 안내", "시설 점검으로 인해 주차장이 임시 폐쇄됩니다. 대중교통 이용 부탁드립니다.", now),
                    createNotice("조조 할인 확대 시행", "평일 조조 할인 적용 시간이 확대됩니다.", now),
                    createNotice("야간 상영 종료 시간 변경", "관객 안전을 위해 야간 상영 종료 시간을 조정합니다.", now),
                    createNotice("시니어 요금 할인 적용 안내", "만 65세 이상 고객 대상 할인 혜택이 제공됩니다.", now),
                    createNotice("IMAX 상영 재개 공지", "리뉴얼된 IMAX관에서 다시 상영을 시작합니다!", now),
                    createNotice("4DX 기기 점검 일정", "4DX 특별관 점검으로 인해 일부 회차 운영이 중단됩니다.", now),
                    createNotice("스낵 세트 신메뉴 출시", "고객의 입맛을 사로잡을 새로운 스낵 세트가 출시되었습니다!", now),
                    createNotice("에어컨 냉방 점검 공지", "여름철 쾌적한 관람을 위한 냉방 시스템 점검이 예정되어 있습니다.", now),
                    createNotice("특별 할인 이벤트 안내", "금요일마다 진행되는 특별 할인 이벤트를 확인해보세요!", now),
                    createNotice("가족석 운영 시간 변경", "가족석 이용 가능 시간이 조정됩니다. 이용에 참고 바랍니다.", now),
                    createNotice("청소년 관람가 영화 안내", "청소년 관람가 영화의 사전 안내 및 추천작 리스트를 확인하세요.", now),
                    createNotice("극장 내 금연 안내", "극장 내 흡연은 법적으로 금지되어 있습니다. 쾌적한 관람 환경 조성에 협조해주세요.", now),
                    createNotice("포스터 수령 일정 변경", "영화 예매 시 제공되는 포스터 수령 가능 날짜가 변경되었습니다.", now),
                    createNotice("시즌 한정 팝콘 출시", "딸기맛 팝콘 등 시즌 한정 팝콘이 판매됩니다!", now),
                    createNotice("티켓 환불 규정 변경", "티켓 환불 규정이 6월부터 일부 변경되오니 확인 바랍니다.", now),
                    createNotice("영화 예매 오류 수정 안내", "일부 고객의 예매 오류가 수정 완료되었습니다. 불편을 드려 죄송합니다.", now),
                    createNotice("정기 소독 및 방역 안내", "매일 극장 전역에 대한 정기 방역을 실시하고 있습니다.", now),
                    createNotice("무인 발권기 위치 변경", "무인 발권기의 위치가 1층 로비로 이동되었습니다.", now),
                    createNotice("장애인 관람석 운영 정책", "장애인 관람석의 예매 및 이용 정책을 새롭게 개편했습니다.", now),
                    createNotice("관람 예절 캠페인 실시", "조용하고 매너 있는 영화 관람을 위한 캠페인이 진행됩니다.", now),
                    createNotice("고객 설문조사 이벤트", "설문에 참여하시면 추첨을 통해 영화 관람권을 드립니다!", now),
                    createNotice("예매 수수료 안내", "일부 카드사의 정책으로 인해 예매 수수료가 부과될 수 있습니다.", now),
                    createNotice("오픈 예정 신관 안내", "신규 상영관이 곧 오픈될 예정입니다. 많은 관심 부탁드립니다!", now),
                    createNotice("단체 관람 안내", "10인 이상 단체 관람 관련 예약 및 혜택 정보를 확인하세요.", now),
                    createNotice("주말 혼잡 시간 안내", "주말 주요 시간대는 혼잡할 수 있으니 여유 있게 방문 부탁드립니다.", now)
            );

            repo.saveAll(notices);
        }
    }

    // 공지 생성 
    private Notice createNotice(String title, String content, LocalDateTime createdAt) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setWriter("관리자");
        notice.setCreatedAt(createdAt);
        notice.setViews(1); // 조회수 초기화
        return notice;
    }
}
