package com.example.demo.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private Movie movie;

    // Getter
    public String getTitle() { return movie.getTitle(); }

    public String getTitleEnglish() { return movie.getTitleEnglish(); }

    public String getRate() { return movie.getRate(); }

    public String getReleaseDate() { return movie.getReleaseDate(); }

    public String getDescription() { return movie.getDescription(); }

    public Integer getRunningTime() { return movie.getRunningTime(); }

    public String getGenre() { return movie.getGenre(); }

    public String getDirector() { return movie.getDirector(); }

    public String getCast() { return movie.getCast(); }

    public Double getScore() { return movie.getScore(); }

    public Long getLikeNumber() { return movie.getLikeNumber(); }

    public String getPoster() { return movie.getPoster(); }

    public String getWideImage() { return movie.getWideImage(); }

    // public String[] getUrls() { return movie.getUrls(); }

    public String getLabel() { return movie.getLabel(); }

    public Double getReserveRate() { return movie.getReserveRate(); }

    public Long getTotalView() { return movie.getTotalView(); }

    public Integer getRank() { return movie.getRank(); }

    // Setter
    public void setId(Long id) { movie.setId(id); }

    public void setTitle(String title) { movie.setTitle(title); }

    public void setTitleEnglish(String titleEnglish) { movie.setTitleEnglish(titleEnglish); }

    public void setRate(String rate) { movie.setRate(rate); }

    public void setReleaseDate(String releaseDate) { movie.setReleaseDate(releaseDate); }

    public void setDescription(String description) { movie.setDescription(description); }

    public void setRunningTime(Integer runningTime) { movie.setRunningTime(runningTime); }

    public void setGenre(String genre) { movie.setGenre(genre); }

    public void setDirector(String director) { movie.setDirector(director); }

    public void setCast(String cast) { movie.setCast(cast); }

    public void setScore(Double score) { movie.setScore(score); }

    public void setLikeNumber(Long likeNumber) { movie.setLikeNumber(likeNumber); }

    public void setPoster(String poster) { movie.setPoster(poster); }

    public void setWideImage(String wideImage) { movie.setWideImage(wideImage); }

    // public void setUrls(String[] urls) { movie.setUrls(urls); }

    public void setLabel(String label) { movie.setLabel(label); }

    public void setReserveRate(Double reserveRate) { movie.setReserveRate(reserveRate); }

    public void setTotalView(Long totalView) { movie.setTotalView(totalView); }

    public void setRank(Integer rank) { movie.setRank(rank); }
}
