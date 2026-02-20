package com.oceanview.resort.service;

import com.oceanview.resort.model.Room;
import com.oceanview.resort.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomRepository repo;

    public Room addRoom(Room r){
        r.setAvailableRooms(r.getTotalRooms());
        return repo.save(r);
    }

    public Room updateRoom(Room r){
        return repo.save(r);
    }

    public List<Room> all(){
        return repo.findAll();
    }

    public Room findById(Long id){
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}