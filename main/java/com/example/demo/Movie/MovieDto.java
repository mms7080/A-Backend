package com.example.demo.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private Movie movie;

    public String getTitle() { return movie.getTitle(); }
    public void setTitle(String title) { movie.setTitle(title); }

    public String getRate() { return movie.getRate(); }
    public void setRate(String rate) { movie.setRate(rate); }

    public String getReleaseDate() { return movie.getReleaseDate(); }
    public void setReleaseDate(String releaseDate) { movie.setReleaseDate(releaseDate); }

    public Long getLikeNumber() { return movie.getLikeNumber(); }
    public void setLikeNumber(Long likeNumber) { movie.setLikeNumber(likeNumber); }

    public String getImage() { return movie.getImage(); }
    public void setImage(String image) { movie.setImage(image); }


    public Integer getRank() {return movie.getRank();}
    public void setRank(Integer rank) {movie.setRank(rank);}
    public String getDescription() {return movie.getDescription();}
    public void setDescription(String description) {movie.setDescription(description);}
    public Double getScore() {return movie.getScore();}
    public void setScore(Double score) {movie.setScore(score);}
    public String getLabel() {return movie.getLabel();}
    public void setLabel(String label) {movie.setLabel(label);}
}
