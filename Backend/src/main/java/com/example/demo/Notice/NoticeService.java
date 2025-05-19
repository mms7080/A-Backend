package com.example.demo.Notice;

import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    private final NoticeRepository repo;

    public NoticeService(NoticeRepository repo) {
        this.repo = repo;
    }

    public List<Notice> getAllNotices() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Notice create(Notice notice) {
        return repo.save(notice);
    }

    public Optional<Notice> getById(Long id) {
        return repo.findById(id);
    }

    public boolean deleteById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public void update(Long id, NoticeDTO dto) {
        Notice notice = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 존재하지 않습니다: " + id));

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setWriter(dto.getWriter());

        repo.save(notice);
    }

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
            createNotice("스낵코너 리뉴얼", "더욱 다양한 메뉴로 새단장한 스낵코너가 곧 오픈합니다. 많은 기대 부탁드립니다!", now)
        );

        repo.saveAll(notices);
    }
}

private Notice createNotice(String title, String content, LocalDateTime createdAt) {
    Notice notice = new Notice();
    notice.setTitle(title);
    notice.setContent(content);
    notice.setWriter("관리자");
    notice.setCreatedAt(createdAt);
    return notice;
}

}
