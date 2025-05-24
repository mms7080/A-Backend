package com.example.demo.Booking.service;

import org.springframework.stereotype.Service;

import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Payment.PaymentRepository;
import com.example.demo.User.UserRepository;

// 예매 시도, 예매 확정, 예매 실패 처리, 예매 내역 조회 등의 기능을 제공
@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;
    private final PaymentRepository paymentRepository;

    private static final int MAX_PASSENGERS_PER_BOOKING = 8;

    public BookingService(BookingRepository bookingRepository, 
                          UserRepository userRepository, 
                          ShowtimeService showtimeService, 
                          SeatService seatService, 
                          PaymentRepository paymentRepository) { 
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showtimeService = showtimeService;
        this.seatService = seatService;
        this.paymentRepository = paymentRepository;
    }

}
