package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Room;
import com.tarasiuk.project.hotel.repository.RoomRepository;
import com.tarasiuk.project.hotel.service.RoomService;
import com.tarasiuk.project.hotel.service.exception.BadRequest;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoomServiceImpl implements RoomService {

    private final CustomLocaleResolver customLocaleResolver;
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository, CustomLocaleResolver customLocaleResolver) {
        this.roomRepository = roomRepository;
        this.customLocaleResolver = customLocaleResolver;
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getById(String room_id) {
        if (room_id == null || room_id.isEmpty()) {
            throw new BadRequest(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.BadRequest", null, LocaleContextHolder.getLocale()),
                    ErrorCodes.BAD_REQUEST.getErrorCode());
        }
        return roomRepository.getOne(Long.parseLong(room_id));
    }

}
