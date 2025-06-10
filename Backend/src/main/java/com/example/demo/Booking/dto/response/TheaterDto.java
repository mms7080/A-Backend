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

    private String address;
    private String subway;
    private String bus;
    private String parkGuide;
    private String parkCheck;
    private String parkFee;
    

    public TheaterDto(Theater theater){
        this.theaterId = theater.getId();
        this.name = theater.getName();
        this.region = theater.getRegion();

        this.address = theater.getAddress();
        this.subway = theater.getSubway();
        this.bus = theater.getBus();
        this.parkGuide = theater.getParkGuide();
        this.parkCheck = theater.getParkCheck();
        this.parkFee = theater.getParkFee();
    }
    
    public static TheaterDto fromEntity(Theater theater){
        if(theater == null){
            return null;
        }
        return new TheaterDto(
            theater.getId(),
            theater.getName(),
            theater.getRegion(),
            
            theater.getAddress(),
            theater.getSubway(),
            theater.getBus(),
            theater.getParkGuide(),
            theater.getParkCheck(),
            theater.getParkFee()
        );
    }

    public TheaterDto(Long theaterId, String name, String region
    , String address, String subway, String bus
    , String parkGuide, String parkCheck, String parkFee){
        this.theaterId = theaterId;
        this.name = name;
        this.region = region;

        this.address = address;
        this.subway = subway;
        this.bus = bus;
        this.parkGuide = parkGuide;
        this.parkCheck = parkCheck;
        this.parkFee = parkFee;
    }
    
}
