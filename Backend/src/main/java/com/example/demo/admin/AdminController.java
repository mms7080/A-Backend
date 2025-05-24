package com.example.demo.admin;

import com.example.demo.User.UserRepository;
import com.example.demo.Event.Event;
import com.example.demo.Event.EventRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Payment.Payment;
import com.example.demo.Payment.PaymentRepository;
import com.example.demo.Store.StoreRepository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MovieRepository movieRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;

    public AdminController(UserRepository userRepository,
            StoreRepository storeRepository,
            MovieRepository movieRepository,
            EventRepository eventRepository,
            PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.movieRepository = movieRepository;
        this.eventRepository = eventRepository;
        this.paymentRepository = paymentRepository;
    }

    // 총 회원 수
    @GetMapping("/user-count")
    public long getUserCount() {
        return userRepository.count();
    }

    // 유저정보
    @GetMapping("/users")
    public List<?> getAllUsers() {
        return userRepository.findAll();
    }

    // 총 스토어 상품 수
    @GetMapping("/store-count")
    public long getStoreCount() {
        return storeRepository.count();
    }

    // 스토어 목록
    @GetMapping("/store")
    public List<?> getAllstores() {
        return storeRepository.findAll();
    }

    // 영화화 목록
    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "rank")); // 랭킹순 정렬
    }

    // 총 영화 수
    @GetMapping("/movie-count")
    public long getMovieCount() {
        return movieRepository.count();
    }

    // 이벤트 전체 리스트
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 이벤트 수 카운트
    @GetMapping("/event-count")
    public long getEventCount() {
        return eventRepository.count();
    }

    // 스토어 매출 전체 조회
    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll(Sort.by(Sort.Direction.DESC, "approvedAt"));
    }

}
