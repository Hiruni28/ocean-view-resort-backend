package com.oceanview.resort.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {

    // PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contact;

    //  FK mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "check_in", nullable = false )
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    private int nights;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private String status = "PENDING";

}
