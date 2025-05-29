package com.example.demo.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.example.demo.Booking.dto.response.ShowtimeDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository,
                           MovieRepository movieRepository,
                           TheaterRepository theaterRepository,
                           SeatRepository seatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    
}