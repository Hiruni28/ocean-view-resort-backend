package com.oceanview.resort.repository;

import com.oceanview.resort.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByRoomType(String roomType);
}
