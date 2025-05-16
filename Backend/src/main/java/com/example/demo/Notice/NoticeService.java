package com.example.demo.Notice;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
