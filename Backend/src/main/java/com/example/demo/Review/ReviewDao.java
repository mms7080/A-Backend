package com.example.demo.Review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;


@Component
public class ReviewDao {
    @Autowired
    private ReviewRepository repo;

    public List<Review> findAll() {
        return repo.findAll();
    }

    public List<Review> findByMovieid(Long movieid) {
        return repo.findByMovieid(movieid);
    }

    public void insert(Review review){
        review.setId(null);
        repo.save(review);
    }

    public void modify(Review review){
        repo.save(review);
    }

    // 초기 데이터 입력
    @PostConstruct
    @Transactional
    public void testReviews() {
        if(repo.count() > 0) return;
        insert(new Review(
            null,
            1L,//필즈 오브 데스티니
            "shin1234",
            "정말 희대의 역작입니다!",
            8,
            2,
            List.of("root","shin1234"),
            "2025-05-01T00:00:00.000"
        ));
        insert(new Review(
            null,
            1L,//필즈 오브 데스티니
            "root",
            "이런 작품은 생전 처음봐요!!",
            7,
            1,
            List.of("shin1234"),
            "2025-05-02T00:00:00.000"
        ));

        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "shin1234",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            10,
            10,
            List.of("root"),
            "2025-05-05T00:00:00.000"
        ));

        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "root",
            "어벤져스 엔드게임 다시 봐도 소름...",
            10,
            8,
            List.of("shin1234"),
            "2025-05-03T00:00:00.000"
        ));

        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "root",
            "어벤져스 엔드게임 너무 재밌어요!...",
            10,
            7,
            List.of("shin1234"),
            "2025-05-04T00:00:00.000"
        ));
    }

}
