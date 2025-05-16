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

    // ✅ 애플리케이션 시작 시 초기 공지 등록
    @PostConstruct
    public void initDefaultNotices() {
        if (repo.count() == 0) {
            Notice notice1 = new Notice();
            notice1.setTitle("사이트 점검 안내");
            notice1.setContent("2025년 6월 1일(토) 새벽 2시부터 4시까지 서버 점검이 진행됩니다.");
            notice1.setWriter("운영자");
            notice1.setCreatedAt(LocalDateTime.now());

            Notice notice2 = new Notice();
            notice2.setTitle("신규 기능 출시");
            notice2.setContent("공지사항 기능이 추가되었습니다. 자유롭게 이용해주세요.");
            notice2.setWriter("관리자");
            notice2.setCreatedAt(LocalDateTime.now());

            repo.save(notice1);
            repo.save(notice2);
        }
    }
    //공지삭제
    public boolean deleteById(Long id) {
    if (repo.existsById(id)) {
        repo.deleteById(id);
        return true;
    }
    return false;
}

}
