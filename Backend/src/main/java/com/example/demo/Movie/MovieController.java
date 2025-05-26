package com.example.demo.Movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class MovieController {

    @Autowired
    MovieDao dao;

    @GetMapping("/movie/all")
    public List<Movie> getAllMovies() {
        return dao.findAll();
    }
    @GetMapping("/movie") 
    public List<Movie> getForMoviePage() {
        return dao.findForMoviePage();
    }

    @GetMapping("/movie/{id}") 
    public Movie getMovieById(@PathVariable Long id) {
        return dao.findById(id);
    }


    @GetMapping("/movie/update/like")
    public String changeLikeNumber(
        @RequestParam(value = "id", defaultValue = "0") Long id,
        @RequestParam(value = "updown", defaultValue = "up") String updown
    ) {
        Movie movie = dao.findById(id);
        if(movie == null)
            return "failed : movie is null";

        if("up".equals(updown))
            movie.setLikeNumber(movie.getLikeNumber() + 1L);
        else if("down".equals(updown))
            movie.setLikeNumber(movie.getLikeNumber() - 1L);
        else
            return "failed : updown = " + updown;
        dao.save(movie);

        return movie.getLikeNumber().toString();
    }
}
