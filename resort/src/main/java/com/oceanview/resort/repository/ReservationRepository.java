package com.oceanview.resort.repository;

import com.oceanview.resort.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {}


