package com.example.demo.Movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MOVIE")
@SequenceGenerator(
    initialValue = 1,
    allocationSize = 1,
    name = "seq_movie",
    sequenceName = "seq_movie"
)
public class Movie {
    @Id
    @GeneratedValue(
        generator = "seq_movie",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private Integer rank;
    private String description;
    private Double score;
    private String label;
    
    
    private String title;
    
    private String rate;
    
    @Column(name = "release_date")
    private String releaseDate;
    
    @Column(name = "like_number")
    private Long likeNumber;
    
    @Column(name = "image_url", length = 300)
    private String image;

    
}