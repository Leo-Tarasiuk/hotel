package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Convenience;
import com.tarasiuk.project.hotel.model.Comfort;
import com.tarasiuk.project.hotel.repository.ComfortRepository;
import com.tarasiuk.project.hotel.repository.ConvenienceRepository;
import com.tarasiuk.project.hotel.service.ConvenienceService;
import com.tarasiuk.project.hotel.service.exception.DeleteException;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import com.tarasiuk.project.hotel.service.exception.SaveException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ConvenienceServiceImpl implements ConvenienceService {

    private final CustomLocaleResolver customLocaleResolver;
    private final ComfortRepository comfortRepository;
    private final ConvenienceRepository convenienceRepository;

    public ConvenienceServiceImpl(ConvenienceRepository convenienceRepository, CustomLocaleResolver customLocaleResolver, ComfortRepository comfortRepository) {
        this.convenienceRepository = convenienceRepository;
        this.customLocaleResolver = customLocaleResolver;
        this.comfortRepository = comfortRepository;
    }

    @Override
    public Convenience save(String name, String standard, String standardPlus, String deluxe,
                            String honeymoonRoom, String juniorSuite, String suite, String description) {
        if (name == null || description == null || name.isEmpty() || description.isEmpty()) {
            throw new NullPointerException();
        }

        name = name.trim();
        description = description.trim();

        List<Comfort> comforts = checkComfort(standard, standardPlus, deluxe, honeymoonRoom, juniorSuite, suite);

        if (comforts.size() == 0) {
            throw new NullPointerException();
        }

        Optional<Convenience> findConvenience = convenienceRepository.findByName(name);
        if (findConvenience.isPresent()) {
            throw new SaveException(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.SaveException", null,
                            LocaleContextHolder.getLocale()), ErrorCodes.SAVE_EXCEPTION.getErrorCode());
        }

        return convenienceRepository.save(Convenience.builder()
                .name(name)
                .comforts(comforts)
                .description(description)
                .build());
    }

    @Override
    public List<Convenience> getAll() {
        return convenienceRepository.findAll();
    }

    private List<Comfort> checkComfort(String... values) {
        List<Comfort> comforts = new ArrayList<>();

        for (String value : values) {
            if (value != null && !value.isEmpty()) {
                value = value.trim();
                comforts.add(comfortRepository.findByName(value));
            }
        }

        return comforts;
    }
}
