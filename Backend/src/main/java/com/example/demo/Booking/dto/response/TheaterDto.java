package com.example.demo.Booking.dto.response;


import com.example.demo.Booking.entity.Theater; 
import lombok.Getter;
import lombok.NoArgsConstructor;


// 극장 정보 전달
@Getter
@NoArgsConstructor
public class TheaterDto {

    private Long theaterId;
    private String name;
    private String region;
    

    public TheaterDto(Theater theater){
        this.theaterId = theater.getId();
        this.name = theater.getName();
        this.region = theater.getRegion();
    }
    
    public static TheaterDto fromEntity(Theater theater){
        if(theater == null){
            return null;
        }
        return new TheaterDto(
            theater.getId(),
            theater.getName(),
            theater.getRegion()
        );
    }

    public TheaterDto(Long theaterId, String name, String region){
        this.theaterId = theaterId;
        this.name = name;
        this.region = region;

    }
     
}
