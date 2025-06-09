package com.example.demo.QnA;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Component
public class QnaDao {

    @Autowired
    private QnaRepository repo;

    public List<Qna> findAll(){
        return repo.findAll();
    }

    public Qna findById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<Qna> findByAuthor(String author){
        return repo.findByAuthor(author);
    }

    public List<Qna> findByReplyto(String replyto){
        return repo.findByReplyto(replyto);
    }

    public void insert(Qna qna){
        qna.setId(null);
        repo.save(qna);
    }

    public void modify(Qna qna){
        repo.save(qna);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @PostConstruct
    @Transactional
    public void testQnas() {
        if(repo.count() > 0)return;

        
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 지정
        String formattedNow = now.format(formatter); // 문자열로 변환

        for(int j=0;j<2;j++)

            insert(new Qna(
            null,
            "shin1234",
            null,
            null,
            "질문 테스트 제목입니다"+(j+1),
            "질문 테스트 내용입니다"+(j+1),
            "2025-05-05 00:00:00"

        ));
        insert(new Qna(
            null,
            "root",
            "shin1234",
            1L,
            "답변 테스트 제목입니다1",
            "답변 테스트 내용입니다1",
            "2025-05-09 00:00:00"
        ));
        insert(new Qna(
            null,
            "WWWWWWWWWWWWWWWW",
            "shin1234",
            1L,
            "답변 테스트 제목입니다1-1",
            "답변 테스트 내용입니다1-1",
            "2025-05-07 00:00:00"
        ));
        insert(new Qna(
            null,
            "root",
            "shin1234",
            2L,
            "답변 테스트 제목입니다2",
            "답변 테스트 내용입니다2",
            "2025-07-01 00:00:00"
        ));
    }
    
}
