package com.example.demo.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
@RequestMapping("/review")
@RestController
public class ReviewController {
    
    @Autowired
    ReviewDao dao;

    @GetMapping("/all")/* 모든 리뷰들을 불러오기 */
    public List<Review> getAllReviews() {
        return dao.findAll();
    }
    @GetMapping("/{id}")/* 영화 ID를 기반으로 리뷰들을 불러오기 */
    public List<Review> getReviewsByMovieID(@PathVariable Long id) {
        return dao.findByMovieid(id);
    }

    @PostMapping("/logic/{movieid}")/* 리뷰 작성하기 */
    public List<Review> writeReview(@Auth User user,@PathVariable Long movieid,
    @RequestBody Map<String, String> data) {

        String content = data.get("content");
        String scoreRaw = data.get("score");
        Integer score = Integer.parseInt(scoreRaw);

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

        return dao.findByMovieid(movieid);
    }

    @PostMapping("/modify/logic")/* 리뷰 수정하기 */
    public List<Review> modifyReview(@RequestBody Map<String, String> data) {

        String idRaw = data.get("id");
        String content = data.get("content");
        String scoreRaw = data.get("score");
        Long id = Long.parseLong(idRaw);
        Integer score = Integer.parseInt(scoreRaw);

        Review original=dao.findById(id);

        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 지정
        String formattedNow = now.format(formatter); // 문자열로 변환

        dao.modify(
            new Review(
            id,
            original.getMovieid(),
            original.getAuthor(),
            content,
            score,
            original.getLikenumber(),
            original.getLikeusers(),
            formattedNow
            )
        );

        return dao.findByMovieid(original.getMovieid());
    }

    @PostMapping("/like/logic/{reviewid}")/* 리뷰 작성하기 */
    public List<String> likeReview(@PathVariable Long reviewid,@RequestBody Map<String, String> data) {

        String liked=data.get("liked");
        String liker=data.get("liker");

        Review targetreview=dao.findById(reviewid);

        if(liked.equals("true")){
            targetreview.getLikeusers().remove(liker);
            targetreview.setLikenumber(targetreview.getLikenumber()-1);
        }
        else if(liked.equals("false")){
            targetreview.getLikeusers().add(liker);
            targetreview.setLikenumber(targetreview.getLikenumber()+1);
        }
        
        dao.modify(targetreview);

        return targetreview.getLikeusers();
    }

}
