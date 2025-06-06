package com.example.demo.QnA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long>  {
    public List<Qna> findByAuthor(String author);
    public List<Qna> findByReplyto(String replyto);
}