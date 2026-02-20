package com.oceanview.resort.service;

import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.repository.ReservationRepository;
import com.oceanview.resort.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;


    // ~~~~~~~~~~~~~CREATE RESERVATION~~~~~~~~~~~~~~~~

    @Transactional
    public Reservation addReservation(Reservation reservation) {

        // Validate room
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            throw new RuntimeException("Room ID is required");
        }

        // Load managed Room entity
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found with ID: " + reservation.getRoom().getId())
                );

        // Check availability
        if (room.getAvailableRooms() <= 0) {
            throw new RuntimeException("No rooms available for " + room.getRoomType());
        }

        // Validate dates
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new RuntimeException("Check-in and check-out dates are required");
        }

        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        if (nights <= 0) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        // Set calculated fields
        reservation.setRoom(room);              // IMPORTANT
        reservation.setRoomType(room.getRoomType());
        reservation.setNights((int) nights);
        reservation.setTotalAmount(room.getPrice() * nights);

        // Update room availability
        room.setAvailableRooms(room.getAvailableRooms() - 1);
        //  Do NOT call roomRepository.save(room)

        // Save reservation
        return reservationRepository.save(reservation);
    }


    // ~~~~~~~~~~~~~~GET ALL RESERVATIONS~~~~~~~~~~~~~~~~~~~~~

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    // GET RESERVATION BY ID

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reservation not found with ID: " + id)
                );
    }


    // ~~~~~~~~~~~~DELETE RESERVATION~~~~~~~~~~~~~~~~~~~~~

    @Transactional
    public void deleteReservation(Long id) {

        Reservation reservation = getReservation(id);
        Room room = reservation.getRoom();

        // Restore availability
        room.setAvailableRooms(room.getAvailableRooms() + 1);
        //  Do NOT call roomRepository.save(room)

        reservationRepository.delete(reservation);
    }


    // ~~~~~~~~~~~~~~UPDATE RESERVATION~~~~~~~~~~~~~~~~~


    @Transactional
    public Reservation updateReservation(Long id, Reservation updated) {

        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Restore old room availability
        Room oldRoom = existing.getRoom();
        oldRoom.setAvailableRooms(oldRoom.getAvailableRooms() + 1);

        // Load new room
        Room newRoom = roomRepository.findById(updated.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (newRoom.getAvailableRooms() <= 0) {
            throw new RuntimeException("No rooms available for " + newRoom.getRoomType());
        }

        // Validate dates
        long nights = ChronoUnit.DAYS.between(
                updated.getCheckIn(),
                updated.getCheckOut()
        );

        if (nights <= 0) {
            throw new RuntimeException("Invalid dates");
        }

        // Update fields
        existing.setGuestName(updated.getGuestName());
        existing.setAddress(updated.getAddress());
        existing.setContact(updated.getContact());
        existing.setRoom(newRoom);
        existing.setRoomType(newRoom.getRoomType());
        existing.setCheckIn(updated.getCheckIn());
        existing.setCheckOut(updated.getCheckOut());
        existing.setNights((int) nights);
        existing.setTotalAmount(newRoom.getPrice() * nights);

        // Reduce new room availability
        newRoom.setAvailableRooms(newRoom.getAvailableRooms() - 1);

        return reservationRepository.save(existing);
    }

    //  UPDATE STATUS ONLY (FOR APPROVAL)
    @Transactional
    public Reservation updateReservationStatus(Long id, String status) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatus(status);
        return reservationRepository.save(r);
    }

}
