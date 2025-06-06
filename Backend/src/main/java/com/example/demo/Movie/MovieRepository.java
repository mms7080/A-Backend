package com.example.demo.Movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    public List<Movie> findAllByOrderByIdAsc();
    public List<Movie> findIdAndRankAndDescriptionAndScoreAndTitleAndRateAndReleaseDateAndLikeNumberAndPosterAndLabelAndReserveRateByOrderByIdAsc();
    public Movie findFirstById(Long id);
    public boolean existsById(Long id);
    public void deleteById(Long id);
}
