package com.example.demo.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Annotations.Auth;
import com.example.demo.User.User;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/review")

@Controller
public class ReviewController {
    
    @Autowired
    ReviewDao dao;

    @Value("${spring.security.cors.site}")
    String corsOrigin;

    @GetMapping("/all")/* 모든 리뷰들을 불러오기 */
    @ResponseBody
    public List<Review> getAllReviews() {
        return dao.findAll();
    }
    @GetMapping("/{id}")/* 영화 ID를 기반으로 리뷰들을 불러오기 */
    @ResponseBody
    public List<Review> getReviewsByMovieID(@PathVariable Long id) {
        return dao.findByMovieid(id);
    }

    @PostMapping("/logic/{movieid}")/* 리뷰 작성하기 */
    public String writeReview(@Auth User user,@PathVariable Long movieid,
    @RequestParam("content") String content,
    @RequestParam("score") Integer score) {

        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 지정
        String formattedNow = now.format(formatter); // 문자열로 변환

        dao.insert(
            new Review(
            null,
            movieid,
            user.getUsername(),
            content,
            score,
            0,
            new ArrayList<>(),
            formattedNow
            )
        );

        return "redirect:"+corsOrigin+"/detail/"+movieid;
    }

}
