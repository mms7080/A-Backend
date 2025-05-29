package com.example.demo.Booking.dto.response;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TheaterDto {

    private Long theaterId;
    private String name;
    private String region;
    // private String location; // 위치 정보는 필요에 따라 추가

    public TheaterDto(Long theaterId, String name, String region) {
        this.theaterId = theaterId;
        this.name = name;
        this.region = region;
    }
    
     
}
