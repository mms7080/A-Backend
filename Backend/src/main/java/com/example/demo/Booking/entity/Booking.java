package com.example.demo.Booking.entity;

import com.example.demo.Payment.Payment; 
import com.example.demo.User.User;     
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
@Table(name = "bookings") 
@Getter
@Setter
@NoArgsConstructor 
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) 
    private User user;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false) 
    private Showtime showtime;

    
    // 💡 FetchType.LAZY를 FetchType.EAGER로 변경합니다.
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "booking_selected_seats", 
            joinColumns = @JoinColumn(name = "booking_id"),       
            inverseJoinColumns = @JoinColumn(name = "seat_id")    
    )
    private Set<Seat> selectedSeats = new HashSet<>();

    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "booking_customer_counts", 
                     joinColumns = @JoinColumn(name = "booking_id")) 
    @MapKeyColumn(name = "customer_category_key") 
    @MapKeyEnumerated(EnumType.STRING)      
    @Column(name = "count_value")                 
    private Map<CustomerCategory, Integer> customerCounts = new HashMap<>();

    @Column(nullable = false, precision = 10, scale = 2) 
    private BigDecimal totalPrice; 

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status; 

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", unique = true)
    private Payment payment;


    @Builder
    public Booking(User user, Showtime showtime, Map<CustomerCategory, Integer> customerCounts,
                   BigDecimal totalPrice, BookingStatus status, Payment payment) {
        this.user = user;
        this.showtime = showtime;
        this.customerCounts = (customerCounts != null) ? new HashMap<>(customerCounts) : new HashMap<>();
        this.totalPrice = totalPrice;
        this.status = status;
        this.payment = payment;
    }

    public void addSelectedSeat(Seat seat) {
        this.selectedSeats.add(seat);}

    
}