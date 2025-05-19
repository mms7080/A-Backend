package com.example.demo.Movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieDao {
    @Autowired
    private MovieRepository movieRep;

    public List<Movie> findAll() {
        return movieRep.findAll();
    }

    public void save(Movie movie) {
        movieRep.save(movie);
    }
}
