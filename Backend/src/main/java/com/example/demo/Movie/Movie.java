package com.example.demo.Movie;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 아래 4개의 import 문 추가
import com.example.demo.Booking.entity.Showtime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
import lombok.Data;
// import lombok.NoArgsConstructor;

@Data
// @AllArgsConstructor
// @NoArgsConstructor
@Entity
@Table(name = "MOVIE")
@SequenceGenerator(
    initialValue = 1,
    allocationSize = 1,
    name = "seq_movie",
    sequenceName = "seq_movie"
)
public class Movie {
    @Id
    @GeneratedValue(
        generator = "seq_movie",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;
    
    private String title;           /* 제목 */

    private String titleEnglish;    /* 영어 제목 */

    private String rate;            /* 상영 등급 */

    @Column(name = "release_date")
    private String releaseDate;     /* 개봉일 */

    @Column(length = 500)
    private String description;     /* 설명 */

    @Column(name = "running_time")
    private Integer runningTime;    /* 상영 시간(분) */

    private String genre;           /* 장르 */

    private String director;        /* 감독 */

    private String cast;            /* 출연진 */

    private Double score;           /* 평점 */

    private Long likeNumber;        /* 좋아요 */

    @Column(name = "poster_url", length = 300)
    private String poster;          /* 작은 포스터 */

    @Column(name = "wide_image_url", length = 300)
    private String wideImage;      /* 큰 이미지 */

    @ElementCollection
    @CollectionTable(
        name = "movie_still_cut",
        joinColumns = @JoinColumn(name="movie_id")
    )
    @Column(name = "still_cut_url")
    private List<String> stillCut = new ArrayList<>();

    @Column(name = "preview_url")
    private String trailer;         /* 예고편 */   

    private String label;           /* MEGA ONLY, DOLBY등 비고 */

    @Column(name = "reserve_rate")
    private Double reserveRate;     /* 예매율(임시) */

    @Column(name = "total_view")
    private Long totalView;         /* 누적관객수 */

    private Integer rank;           /* 순위(임시) */
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // cascade 추가 삭제시 showtime->booking,seat도 같이 삭제 무결성제약조건 해결을 위해 추가.
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Api 응답시 무한 순환 참조 방지
    private List<Showtime> showtimes = new ArrayList<>();

    // 1. JPA를 위한 기본 생성자 // cascade때문에 만듦은 이게 없으면 기존 생성자가 총 20개인데 cascade 추가 삭제때문에 1개가 더 생겨 21개로 되서 이게 없으면 movie컨트롤,DAO에서 오류가남 생성자가 안맞아서
    public Movie() {
    }

    // 2. 기존 코드(MovieDao, MovieController)에서 필요로 하는 생성자
    public Movie(Long id, String title, String titleEnglish, String rate, String releaseDate, String description,
                 Integer runningTime, String genre, String director, String cast, Double score, Long likeNumber,
                 String poster, String wideImage, List<String> stillCut, String trailer, String label,
                 Double reserveRate, Long totalView, Integer rank, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.titleEnglish = titleEnglish;
        this.rate = rate;
        this.releaseDate = releaseDate;
        this.description = description;
        this.runningTime = runningTime;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.score = score;
        this.likeNumber = likeNumber;
        this.poster = poster;
        this.wideImage = wideImage;
        this.stillCut = stillCut;
        this.trailer = trailer;
        this.label = label;
        this.reserveRate = reserveRate;
        this.totalView = totalView;
        this.rank = rank;
        this.createdAt = createdAt;
    }
    
}