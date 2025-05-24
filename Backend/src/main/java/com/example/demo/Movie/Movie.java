package com.example.demo.Movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    // @Column(name = "urls")
    // private String[] urls;          /* 예고편이나 스틸컷 등 기타 링크 */

    private String label;           /* MEGA ONLY, DOLBY등 비고 */

    @Column(name = "reserve_rate")
    private Double reserveRate;     /* 예매율(임시) */

    @Column(name = "total_view")
    private Long totalView;         /* 누적관객수 */

    private Integer rank;           /* 순위(임시) */
    
    
    
    
    
    

    
}