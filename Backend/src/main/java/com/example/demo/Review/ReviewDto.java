package com.example.demo.Review;

import java.util.List;

public class ReviewDto {

    private Review review;
    
    public Long getMovieid() { return review.getMovieid(); }

    public String getAuthor() { return review.getAuthor(); }

    public String getContent() { return review.getContent(); }

    public Integer getScore() { return review.getScore(); }

    public Integer getLikenumber() { return review.getLikenumber(); }

    public List<String> getLikeusers() { return review.getLikeusers(); }

    public String getWritetime() { return review.getWritetime(); }
    

    public void setId(Long id) { review.setId(id); }

    public void setMovieid(Long movieid) { review.setMovieid(movieid); }

    public void setAuthor(String author) { review.setAuthor(author); }

    public void setContent(String content) { review.setContent(content); }

    public void setScore(Integer score) { review.setScore(score); }

    public void setLikenumber(Integer likenumber) { review.setLikenumber(likenumber); }
    
    public void setLikeusers(List<String> likeusers) { review.setLikeusers(likeusers); }

    public void setWritetime(String writetime) { review.setWritetime(writetime); }

}
