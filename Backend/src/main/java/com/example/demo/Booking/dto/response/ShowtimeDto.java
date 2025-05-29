package com.example.demo.Booking.dto.response;

import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime; 
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 상영 시간표 전달
@Getter 
@NoArgsConstructor 
public class ShowtimeDto {

    private Long showtimeId;
    private MovieBookingInfoDto movieInfo;
    private TheaterDto theateInfo;
    private String startTime;
    private String auditoriumName;
    private Integer totalSeats;
    private Integer avilableSeats;

    public ShowtimeDto(Showtime showtime){
        this.showtimeId = showtime.getId();

        if(showtime.getMovie() != null){
            this.movieInfo = MovieBookingInfoDto.fromEntity(showtime.getMovie());
        }

        if(showtime.getTheater() != null){
            this.theateInfo = TheaterDto.fromEntity(showtime.getTheater());
        }

        if(showtime.getStartTime() != null){
            this.startTime = showtime.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm0"));
        }
        this.auditoriumName = showtime.getAuditoriumName();

        if(showtime.getSeats() != null){
            this.totalSeats = showtime.getSeats().size();
            this.avilableSeats = (int) showtime.getSeats().stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .count();
        } else{
            this.totalSeats =0;
            this.avilableSeats =0;
        }

    }

    public ShowtimeDto(Long shotimeId, MovieBookingInfoDto movieInfo, TheaterDto theaterInfo,String startTime, String auditoriumName, Integer totalSeats, Integer avilableSeats ){
        this.showtimeId = shotimeId;
        this.movieInfo = movieInfo;
        this.theateInfo = theaterInfo;
        this.startTime = startTime;
        this.auditoriumName = auditoriumName;
        this.totalSeats = totalSeats;
        this.avilableSeats = avilableSeats;
    }

    public static ShowtimeDto fromEntity(Showtime showtime){
        if(showtime == null){
            return null;
        }
        return new ShowtimeDto(showtime);
    }
   
}