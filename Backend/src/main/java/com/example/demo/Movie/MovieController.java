package com.example.demo.Movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    MovieDao dao;

    @GetMapping("/get")
    public List<Movie> getAllMovies() {
        return dao.findAll();
    }

    @GetMapping("/update/like")
    public String changeLikeNumber(
        @RequestParam(value = "id", defaultValue = "0") Long id,
        @RequestParam(value = "updown", defaultValue = "up") String updown
    ) {
        Movie movie = dao.findById(id);
        movie.setLikeNumber(movie.getLikeNumber() + (updown == "up" ? 1L : -1L));
        dao.save(movie);

        return "succeed";
    }
    

    @GetMapping("/insert/test")
    @Transactional
    public String insertTestMovies() {
        dao.save(new Movie(null, "Fields of Destiny", "", "0000.00.00", 5300L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700795880/catalog/1600659718750367744/xiry6ufbjttckqxpfzrw.jpg"));
        dao.save(new Movie(null, "Killer Advice", "", "0000.00.00", 2100L, "https://res.cloudinary.com/upwork-cloud/image/upload/c_scale,w_1000/v1700796426/catalog/1600659718750367744/iqmiudmmo6s7zcofwmpf.jpg"));
        dao.save(new Movie(null, "InterStella", "12", "2014.11.06", 1500L, "https://rukminim2.flixcart.com/image/850/1000/l2dmky80/poster/y/f/b/small-poster-interstellar-sl407-wall-poster-13x19-inches-matte-original-imagdqezkfchjkhz.jpeg?q=20&crop=false"));
        dao.save(new Movie(null, "My Name is Alfred Hitchcock", "", "0000.00.00", 1300L, "https://www.mvtimes.com/mvt/uploads/2024/11/film-my-name-alfred-hitchcock-2.jpg"));
        dao.save(new Movie(null, "어벤져스 엔드게임", "12", "2019.04.24", 986L, "https://upload.wikimedia.org/wikipedia/ko/thumb/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/1200px-%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"));
        dao.save(new Movie(null, "범죄도시 4", "15", "2024.04.24", 734L, "https://i.namu.wiki/i/KwJ2dfIySu2k8JWlK3nD-gS7A9G-2I2EWKkNjoVRqaHabjK88STUo8FXi545XV6Pe8ERSX5DjF4e5k0IkOvznQ.webp"));
        dao.save(new Movie(null, "귀멸의칼날 무한성편", "19", "2025.08.22", 521L, "https://i.namu.wiki/i/YvPBZ1kzk8Dku4HhOC2FGB7xKVXj5bpg8cSdRWsAZg-3Knqu5LcWJchrZDIVmz-08V3OV9uFLMfCRNCZRcnTxQ.webp"));
        dao.save(new Movie(null, "승부", "12", "2025.03.26", 342L, "https://img.megabox.co.kr/SharedImg/2025/03/27/O6RnDMOAnUw6geDdlaAXRlkqgy0mSSDb_420.jpg"));
        return "succeed";
    }
}
