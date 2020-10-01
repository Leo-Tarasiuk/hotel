package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Room;
import org.apache.tomcat.jni.File;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface RoomService {

    List<Room> getRooms();

    Room getById(String room_id);
}
