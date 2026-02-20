package com.oceanview.resort.controller;

import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService service;

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Reservation r) {
        if (r.getRoom() == null || r.getRoom().getId() == null) {
            return ResponseEntity.badRequest().body("Room ID is required and must exist");
        }
        try {
            Reservation saved = service.addReservation(r);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public List<Reservation> getAll() {
        return service.getAllReservations();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            Reservation r = service.getReservation(id);
            return ResponseEntity.ok(r);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Reservation updated) {

        try {
            Reservation saved = service.updateReservation(id, updated);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  UPDATE STATUS (ADMIN APPROVAL)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status"); // "APPROVED" or "REJECTED"
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body("Status is required");
            }
            Reservation updated = service.updateReservationStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteReservation(id);
            return ResponseEntity.ok("Reservation deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

