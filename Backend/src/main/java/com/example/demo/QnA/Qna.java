package com.example.demo.QnA;

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
@Table(name = "QNA")
@SequenceGenerator(
    initialValue = 1,
    allocationSize = 1,
    name = "seq_qna",
    sequenceName = "seq_qna"
)

public class Qna {
    @Id
    @GeneratedValue(
        generator = "seq_qna",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private String author; /* 작성자의 아이디(username) */

    private String replyto; /* 답글일 경우, 원글의 작성자 아이디(username), 답글이 아니면 null */

    private Long replytoid; /* 답글일 경우, 원글의 id, 답글이 아니면 null */

    private String title; /* QnA의 제목 */

    @Column(length = 500)
    private String content; /* QnA의 내용 */

    private String writetime; /* QnA 작성 날짜, 시간 */
}
