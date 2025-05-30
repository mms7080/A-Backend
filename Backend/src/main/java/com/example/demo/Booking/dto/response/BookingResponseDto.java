package com.example.demo.Booking.dto.response;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.CustomerCategory;
import com.example.demo.Booking.entity.Seat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BookingResponseDto {

    private Long bookingId;

    // == 영화 상세 정보 ==
    private String movieTitle;
    private String movieTitleEnglish;
    private String posterUrl;
    private String ageRating;
    private String genre;
    private String director;
    private String cast;
    private Double score;
    private Double reserveRate;
    private Long totalView;
    private Integer runningTime;

    // == 상영 정보 ==
    private String theaterName;
    private String auditoriumName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showtime;

    // == 예매 상세 내역 ==
    private List<String> selectedSeatNames;
    private Map<CustomerCategory, Integer> customerCounts;
    private BigDecimal totalPrice;
    private String bookingStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingTime;

    public BookingResponseDto(Booking booking) {
        this.bookingId = booking.getId();

        // 상영 정보 및 영화 정보 설정
        if (booking.getShowtime() != null) {
            this.showtime = booking.getShowtime().getStartTime();
            this.auditoriumName = booking.getShowtime().getAuditoriumName();
            
            if (booking.getShowtime().getTheater() != null) {
                this.theaterName = booking.getShowtime().getTheater().getName();
            }

            // MovieBookingInfoDto를 통해 영화 상세 정보 매핑
            if (booking.getShowtime().getMovie() != null) {
                MovieBookingInfoDto movieInfo = MovieBookingInfoDto.fromEntity(booking.getShowtime().getMovie());
                this.movieTitle = movieInfo.getTitle();
                this.movieTitleEnglish = movieInfo.getTitleEnglish();
                this.posterUrl = movieInfo.getPosterUrl();
                this.ageRating = movieInfo.getAgeRating();
                this.genre = movieInfo.getGenre();
                this.director = movieInfo.getDirector();
                this.cast = movieInfo.getCast();
                this.score = movieInfo.getScore();
                this.reserveRate = movieInfo.getReserveRate();
                this.totalView = movieInfo.getTotalView();
                this.runningTime = movieInfo.getRunningTime();
            }
        }

        // 선택 좌석 정보 설정
        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            this.selectedSeatNames = booking.getSelectedSeats().stream()
                    .map(Seat::getFullSeatName)
                    .sorted() // 좌석 이름 순으로 정렬
                    .collect(Collectors.toList());
        } else {
            this.selectedSeatNames = Collections.emptyList();
        }

        this.customerCounts = booking.getCustomerCounts();
        this.totalPrice = booking.getTotalPrice();
        this.bookingStatus = (booking.getStatus() != null) ? booking.getStatus().name() : null;
        this.bookingTime = booking.getBookingTime();
    }

    public static BookingResponseDto fromEntity(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingResponseDto(booking);
    }
}