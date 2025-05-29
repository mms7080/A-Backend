package com.example.demo.Review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/review")

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

}
