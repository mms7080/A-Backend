package com.example.demo.QnA;

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

    public List<Qna> findByReplytoid(Long replytoid){
        return repo.findByReplytoid(replytoid);
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
    }
    
}
