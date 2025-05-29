package com.example.demo.Review;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REVIEW")
@SequenceGenerator(
    initialValue = 1,
    allocationSize = 1,
    name = "seq_review",
    sequenceName = "seq_review"
)

public class Review {
    @Id
    @GeneratedValue(
        generator = "seq_review",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private String author; /* 작성자의 아이디(username) */

    private String content; /* 리뷰의 내용 */

    private Integer score; /* 평점 */

    private Integer likenumber; /* 좋아요 숫자 */

    /* 좋아요 누른 아이디 목록 (1:N 구조) → 별도 테이블 생성됨 */
    @ElementCollection
    @CollectionTable(
        name = "like_users", /* 생성될 테이블 이름 */
        joinColumns = @JoinColumn(name = "like_users_id") /* 외래 키 */
    )
    private List<String> likeusers=new ArrayList<>();

    private String writetime; /* 리뷰 작성 날짜, 시간 */
}
