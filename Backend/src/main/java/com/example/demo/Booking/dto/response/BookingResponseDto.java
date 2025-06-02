package com.example.demo.Booking.dto.response;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.CustomerCategory;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.Showtime; 
import com.example.demo.User.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale; 
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
    private String theaterRegion;
    private String auditoriumName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showtime;
    private String showtimeDate;
    private String showtimeTime;

    // == 예매 좌석 및 인원 정보 ==
    private List<String> selectedSeatNames;
    private Map<CustomerCategory, Integer> customerCounts;
    private List<PriceDetailDto> priceDetails;

    // 결제 정보
    private BigDecimal totalPrice;
    private String bookingStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingTime;

    // 토스페이먼츠 연동을 위한 필드
    private String paymentOrderId;
    private String paymentOrderName;
    private String customerName;
    private String customerEmail;

    public BookingResponseDto(Booking booking, Map<CustomerCategory, BigDecimal> pricePolicy) {
        this.bookingId = booking.getId();

        Showtime showtimeEntity = booking.getShowtime();
        if (showtimeEntity != null) {
            this.showtime = showtimeEntity.getStartTime();
            this.showtimeDate = formatShowtimeDate(showtimeEntity.getStartTime());
            this.showtimeTime = formatShowtimeTime(showtimeEntity.getStartTime());
            this.auditoriumName = showtimeEntity.getAuditoriumName();

            if (showtimeEntity.getTheater() != null) {
                this.theaterName = showtimeEntity.getTheater().getName();
                this.theaterRegion = showtimeEntity.getTheater().getRegion(); 
            }

            if (showtimeEntity.getMovie() != null) {
                MovieBookingInfoDto movieInfo = MovieBookingInfoDto.fromEntity(showtimeEntity.getMovie());
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

        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            this.selectedSeatNames = booking.getSelectedSeats().stream()
                    .map(Seat::getFullSeatName)
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            this.selectedSeatNames = Collections.emptyList();
        }

        this.customerCounts = booking.getCustomerCounts();
        this.totalPrice = booking.getTotalPrice();
        this.bookingStatus = (booking.getStatus() != null) ? booking.getStatus().name() : null;
        this.bookingTime = booking.getBookingTime();

        // 가격 상세 내역 생성
        this.priceDetails = new ArrayList<>();
        if (booking.getCustomerCounts() != null && !booking.getCustomerCounts().isEmpty() && pricePolicy != null) {
            booking.getCustomerCounts().forEach((category, count) -> {
                if (count > 0) {
                    BigDecimal unitPrice = pricePolicy.get(category);
                    if (unitPrice != null) {
                        this.priceDetails.add(new PriceDetailDto(
                            getDisplayCategoryName(category),
                            count,
                            unitPrice,
                            unitPrice.multiply(new BigDecimal(count))
                        ));
                    }
                }
            });
        }

        this.paymentOrderId = String.valueOf(booking.getId());
        this.paymentOrderName = generateOrderName(booking);
        
        User user = booking.getUser();
        if (user != null) {
            this.customerName = user.getName();
            this.customerEmail = user.getEmail();
        }
    }
    
    private String formatShowtimeDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        // 요일을 한국어로 표시 (예: "월", "화")
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN);
        return dateTime.format(dateFormatter);
    }

    private String formatShowtimeTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(timeFormatter);
    }
    
    private String getDisplayCategoryName(CustomerCategory category) {
        return switch (category) {
            case ADULT -> "성인";
            case YOUTH -> "청소년";
            case SENIOR -> "경로";
            case DISABLED -> "우대";
            default -> category.name(); 
        };
    }
    
    private String generateOrderName(Booking booking) {
        String movieTitleDisplay = "영화 티켓"; // 기본값
        if (booking.getShowtime() != null && booking.getShowtime().getMovie() != null) {
            movieTitleDisplay = booking.getShowtime().getMovie().getTitle();
        }
        int seatCount = booking.getSelectedSeats() != null ? booking.getSelectedSeats().size() : 0;
        if (seatCount > 0) {
            return String.format("%s %d매", movieTitleDisplay, seatCount);
        }
        return movieTitleDisplay;
    }

    public static BookingResponseDto fromEntity(Booking booking, Map<CustomerCategory, BigDecimal> pricePolicy) {
        if (booking == null) {
            return null;
        }
        return new BookingResponseDto(booking, pricePolicy);
    }
}