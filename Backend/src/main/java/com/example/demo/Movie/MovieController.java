package com.example.demo.Movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class MovieController {

    @Autowired
    MovieDao dao;

    @Value("${movie.upload.dir}")
    private String uploadDir;

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
    
    @PostMapping("movie/upload")
    public ResponseEntity<?> uploadMovie(
            @RequestParam("title") String title,
            @RequestParam("titleEnglish") String titleEnglish,
            @RequestParam("rate") String rate,
            @RequestParam("releaseDate") String releaseDate,
            @RequestParam("description") String description,
            @RequestParam("runningTime") Integer runningTime,
            @RequestParam("genre") String genre,
            @RequestParam("director") String director,
            @RequestParam("cast") String cast,
            @RequestParam("poster") MultipartFile poster,
            @RequestParam("wideImage") MultipartFile wideImage,
            @RequestParam("stillCut") List<MultipartFile> stillCut,
            @RequestParam("trailer") String trailer,
            @RequestParam("label") String label
    ) {
        try {
            String posterUrl = null, wideImageUrl = null;
            List<String> steelCutUrl = new ArrayList<>();

            // 이미지 저장 경로
            Path uploadPath = Paths.get("src/main").toAbsolutePath().getParent().getParent().resolve(uploadDir);
            System.out.println(uploadPath.toString());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 이미지 저장 및 URL 구성
            for (MultipartFile image : new MultipartFile[] {poster, wideImage}) {
                String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
                Path filePath = uploadPath.resolve(filename);
                image.transferTo(filePath.toFile());
                if(posterUrl == null) posterUrl = ("/images/movie/" + filename);
                else if(wideImageUrl == null) wideImageUrl = ("/images/movie/" + filename);
                else throw new IOException("url is already full");
            }
            for (MultipartFile image : stillCut) {
                String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
                Path filePath = uploadPath.resolve(filename);
                image.transferTo(filePath.toFile());
                steelCutUrl.add("/images/movie/" + filename);
            }
            Movie movie = new Movie(null, title, titleEnglish, rate, releaseDate, description, runningTime, genre, director, cast, 0.0, 0L, posterUrl, wideImageUrl, steelCutUrl, trailer, label, 0.0, 0L, 0, LocalDateTime.now());
            dao.save(movie);
            return ResponseEntity.ok("업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("저장 실패: " + e.getMessage());
        }
    }

    // 영화 삭제 
    @DeleteMapping("movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        if (!dao.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        dao.deleteById(id);
        return ResponseEntity.ok("삭제 완료");
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

    @GetMapping("movie/reserveRate/{id}")
    public double getReserveRate(@PathVariable Long id) {
        var movie = dao.findById(id);
        if(movie == null) return -1.0;

        List<Showtime> showtimes = movie.getShowtimes();
        if(showtimes == null || showtimes.isEmpty())
            return 0.0;
        

        List<Seat> seats = new ArrayList<>();
        for(Showtime showtime : showtimes) 
            seats.addAll(showtime.getSeats());
        
        int totalSeats = seats.size();
        if(totalSeats <= 0) return 0.0;

        Long reservedSeats = seats.stream().filter(
            t -> t.getStatus() == SeatStatus.RESERVED
        ).count();

        double reserveRate = (double) reservedSeats / totalSeats;
        if(reserveRate > 0.0 && reserveRate < 0.05) return 0.1;
        else if(reserveRate >= 99.95 && reserveRate < 100.0) return 99.9;
        else return Math.round(reserveRate * 10) / 10;
    }
}
