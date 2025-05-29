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
    private String movieTitle;
    private String posterUrl;
    private String theaterName;
    private String auditoriumName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime showtime;

    private List<String> selectedSeatNames;
    private Map<CustomerCategory, Integer> customerCounts;
    private BigDecimal totalPrice;
    private String bookingStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingTime;

    
    public BookingResponseDto(Booking booking) {
        this.bookingId = booking.getId();

        if (booking.getShowtime() != null) {
            ShowtimeDto showtimeDto = ShowtimeDto.fromEntity(booking.getShowtime());
            if (showtimeDto != null) {
                if (showtimeDto.getMovieInfo() != null) {
                    this.movieTitle = showtimeDto.getMovieInfo().getTitle();
                    this.posterUrl = showtimeDto.getMovieInfo().getPosterUrl();
                }
                if (showtimeDto.getTheaterInfo() != null) {
                    this.theaterName = showtimeDto.getTheaterInfo().getName();
                }
                this.auditoriumName = showtimeDto.getAuditoriumName();
            }
            this.showtime = booking.getShowtime().getStartTime();
        }

        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            this.selectedSeatNames = booking.getSelectedSeats().stream()
                    .map(Seat::getFullSeatName)
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