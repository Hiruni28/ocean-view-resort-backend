package com.oceanview.resort.controller;

import com.oceanview.resort.model.Room;
import com.oceanview.resort.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    @Autowired
    RoomService service;

    // Correct absolute path + ending slash
    private final String UPLOAD_DIR = "D:/oceanview-resort/backend/uploads/";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> addRoom(
            @RequestParam String roomType,
            @RequestParam double price,
            @RequestParam int totalRooms,
            @RequestParam String description,
            @RequestParam MultipartFile image
    ) throws IOException {

        // Validate image type
        String contentType = image.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/png") &&
                        !contentType.equals("image/jpeg") &&
                        !contentType.equals("image/jpg"))) {
            throw new IOException("Only PNG, JPG, and JPEG files are allowed");
        }

        // Create folder if missing
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save image
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create room
        Room r = new Room();
        r.setRoomType(roomType);
        r.setPrice(price);
        r.setTotalRooms(totalRooms);
        r.setAvailableRooms(totalRooms);
        r.setDescription(description);
        r.setImage(fileName);

        Room savedRoom = service.addRoom(r);
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestParam String roomType,
            @RequestParam double price,
            @RequestParam int totalRooms,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Room existingRoom = service.findById(id);
        if (existingRoom == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate new image type
        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/png") &&
                            !contentType.equals("image/jpeg") &&
                            !contentType.equals("image/jpg"))) {
                throw new IOException("Only PNG, JPG, and JPEG files are allowed");
            }
        }

        // Update fields
        existingRoom.setRoomType(roomType);
        existingRoom.setPrice(price);
        existingRoom.setDescription(description);

        int difference = totalRooms - existingRoom.getTotalRooms();
        existingRoom.setTotalRooms(totalRooms);
        existingRoom.setAvailableRooms(existingRoom.getAvailableRooms() + difference);

        // Update image if provided
        if (image != null && !image.isEmpty()) {

            // Delete old image
            Path oldImagePath = Paths.get(UPLOAD_DIR + existingRoom.getImage());
            if (Files.exists(oldImagePath)) {
                Files.delete(oldImagePath);
            }

            // Save new image
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            existingRoom.setImage(fileName);
        }

        Room updatedRoom = service.updateRoom(existingRoom);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping
    public ResponseEntity<List<Room>> all() {
        return ResponseEntity.ok(service.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Long id) {
        Room room = service.findById(id);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Room room = service.findById(id);
        if (room != null && room.getImage() != null) {
            try {
                Path imagePath = Paths.get(UPLOAD_DIR + room.getImage());
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
