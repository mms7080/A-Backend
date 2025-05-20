package com.example.demo.Booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dao.ReservationDao;
import com.example.demo.Booking.entity.Reservation;

@Service
public class ReservationService {
    private final ReservationDao dao;

    public ReservationService(ReservationDao dao){
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Reservation getById(Long id){
        return dao.findById(id);
    }

    @Transactional Reservation create(Reservation r){
        return dao.save(r);
    }

    @Transactional
    public Reservation update(Long id, Reservation updated){
        Reservation existing = dao.findById(id);
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerCategory(updated.getCustomerCategory());
        existing.setReservedAt(updated.getReservedAt());
        existing.setSeats(updated.getSeats());
        return dao.save(existing);
    }

    @Transactional
    public void delete(Long id){
        dao.deleteById(id);
    }

    // 커스텀 조회
    @Transactional(readOnly = true)
    public List<Reservation> findByCustomerName(String name){
        return dao.findCustomerName(name);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findBySeatId(Long seatId){
        return dao.findBySeatId(seatId);
    }

    @Transactional(readOnly = true)
        public List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to){
            return dao.findByReservedAtBetween(from, to);
        }
    
    

}
