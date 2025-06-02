package com.example.demo.Review;

import java.util.ArrayList;
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

    public Review findById(Long id) {
        return repo.findById(id).orElse(null);
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

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
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
            "2025-05-01 00:00:00"
        ));
        insert(new Review(
            null,
            1L,//필즈 오브 데스티니
            "root",
            "이런 작품은 생전 처음봐요!!",
            7,
            1,
            List.of("shin1234"),
            "2025-05-02 00:00:00"
        ));

        for(int i=0;i<25;i++){
            List<String> templikeusers=new ArrayList<>();
            for(int j=0;j<i;j++)
                templikeusers.add("user"+j);
           insert(new Review(
            null,
            1L,//필즈 오브 데스티니
            "root"+i,
            "이런 작품은 생전 처음봐요!!",
            i%10+1,
            i,
            templikeusers,
            "2025-05-02 00:00:00"
        )); 
        }

        List<String> templikeusers=new ArrayList<>();
        for(int j=0;j<100;j++)
            templikeusers.add("user"+j);
        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "shin1234",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            10,
            100,
            templikeusers,
            "2025-05-05 00:00:00"
        ));

        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "lovelytourist84",
            "어벤져스 엔드게임 다시 봐도 소름...",
            9,
            1,
            List.of("shin1234"),
            "2025-05-03 00:00:00"
        ));

        insert(new Review(
            null,
            5L,//어벤져스 : 엔드게임
            "root",
            "어벤져스 엔드게임 너무 재밌어요!...",
            8,
            1,
            List.of("shin1234"),
            "2025-05-04 00:00:00"
        ));

        templikeusers=new ArrayList<>();
        for(int j=0;j<27;j++)
            templikeusers.add("user"+j);
        insert(new Review(
            null,
            2L,//킬러 어드바이스
            "shin1234",
            "정말 희대의 역작입니다!",
            7,
            27,
            templikeusers,
            "2025-05-01 00:00:00"
        ));
        insert(new Review(
            null,
            2L,//킬러 어드바이스
            "root",
            "형편없네요",
            4,
            1,
            List.of("shin1234"),
            "2025-05-02 00:00:00"
        ));

        insert(new Review(
            null,
            3L,//인터스텔라
            "shin1234",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            9,
            1,
            List.of("root"),
            "2025-05-05 00:00:00"
        ));

        insert(new Review(
            null,
            3L,//인터스텔라
            "root",
            "인터스텔라 다시 봐도 소름...",
            7,
            1,
            List.of("shin1234"),
            "2025-05-03 00:00:00"
        ));

        insert(new Review(
            null,
            3L,//인터스텔라
            "kerasis23",
            "인터스텔라 너무 재밌어요!...",
            8,
            1,
            List.of("shin1234"),
            "2025-05-04 00:00:00"
        ));

        insert(new Review(
            null,
            4L,//내 이름은 알프레드 히치콕
            "kkj7584",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            7,
            1,
            List.of("root"),
            "2025-05-05 00:00:00"
        ));

        insert(new Review(
            null,
            4L,//내 이름은 알프레드 히치콕
            "lovelytourist84",
            "노잼이네요",
            3,
            1,
            List.of("shin1234"),
            "2025-05-03 00:00:00"
        ));

        insert(new Review(
            null,
            4L,//내 이름은 알프레드 히치콕
            "mns7080",
            "내 이름은 알프레드 히치콕 너무 재밌어요!...",
            10,
            1,
            List.of("shin1234"),
            "2025-05-04 00:00:00"
        ));


        //movieid = 6 범죄도시는 건너뜀

        insert(new Review(
            null,
            7L,//귀멸의 칼날 무한성편
            "kkj7584",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            9,
            1,
            List.of("root"),
            "2025-05-05 00:00:00"
        ));

        insert(new Review(
            null,
            7L,//귀멸의 칼날 무한성편
            "lovelytourist84",
            "그저 그렇네요.",
            5,
            1,
            List.of("shin1234"),
            "2025-05-03 00:00:00"
        ));

        insert(new Review(
            null,
            7L,//귀멸의 칼날 무한성편
            "mms7080",
            "귀멸의 칼날 너무 재밌어요!...",
            8,
            1,
            List.of("shin1234"),
            "2025-05-04 00:00:00"
        ));

        insert(new Review(
            null,
            8L,//승부
            "kkj7584",
            "이 영화 정말 최고였어요! 감동받았습니다.",
            8,
            1,
            List.of("root"),
            "2025-05-05 00:00:00"
        ));

        insert(new Review(
            null,
            8L,//승부
            "lovelytourist84",
            "재미없어요.",
            1,
            1,
            List.of("shin1234"),
            "2025-05-03 00:00:00"
        ));

        insert(new Review(
            null,
            8L,//승부
            "mms7080",
            "승부 너무 재밌어요!...",
            9,
            1,
            List.of("shin1234"),
            "2025-05-04 00:00:00"
        ));
    }

}
