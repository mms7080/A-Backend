package com.example.demo.Booking.dto;

import com.example.demo.Booking.entity.Theater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterFilterDto {
    
    private Long id;
    private String name;
    private String region;

    public static TheaterFilterDto fromEntity(Theater theater){
        if (theater == null) {
            return null;
        }
        return new TheaterFilterDto(
            theater.getId(),
            theater.getName(),
            theater.getRegion()
        );
    }
}
