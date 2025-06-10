package com.example.demo.QnA;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Annotations.Auth;
import com.example.demo.User.User;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/qna")
@RestController

public class QnaController {
    @Autowired
    QnaDao dao;

    @GetMapping("/all")/* 모든 Qna들을 불러오기 */
    public List<Qna> getAllQnas() {
        return dao.findAll();
    }

    @GetMapping("/author/{author}")/* 글 작성자를 기반으로 Qna들을 불러오기 */
    public List<Qna> getQnasByAuthor(@PathVariable String author) {
        return dao.findByAuthor(author);
    }

    @GetMapping("/reply/{replyto}")/* 답변 Qna들을 원글 작성자 기반으로 불러오기 */
    public List<Qna> getQnasByReplyto(@PathVariable String replyto) {
        return dao.findByReplyto(replyto);
    }

    @PostMapping("/write/logic")/* Qna 작성하기 */
    public Qna writeQna(
        @Auth User user,
        @RequestBody Map<String, String> data
        ) {

        String title = data.get("title");
        String content = data.get("content");
        String replyto = data.get("replyto");
        String replytoid = data.get("replytoid");

        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 지정
        String formattedNow = now.format(formatter); // 문자열로 변환

        Qna newqna=new Qna(
            null,
            user.getUsername(),
            replyto,
            (replytoid!=null)?Long.valueOf(replytoid):null,
            title,
            content,
            formattedNow,
            false
            );
        dao.insert(newqna);

        return newqna;
    }

    @PostMapping("/modify/logic")/* Qna 수정하기 */
    public Qna modifyQna(
        @RequestBody Map<String,String> data
    ) {

        Long id=Long.valueOf(data.get("id"));
        String title=data.get("title");
        String content=data.get("content");

        Qna original=dao.findById(id);

        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 지정
        String formattedNow = now.format(formatter); // 문자열로 변환

        original.setTitle(title);
        original.setContent(content);
        original.setWritetime(formattedNow);

        dao.modify(original);

        return original;/* 수정본을 리턴 */
    }

    @GetMapping("/delete/logic/{id}")/* Qna 삭제하기 */
    public Qna deleteQna(@PathVariable Long id) {

        Qna original=dao.findById(id);

        original.setDeleted(true);

        dao.modify(original);

        return original;/* 삭제본을 리턴 */

    }
}
