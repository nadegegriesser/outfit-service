package de.griesser.outfit.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.griesser.outfit.service.config.ServiceProperties;
import de.griesser.outfit.service.api.City;
import de.griesser.outfit.service.api.CityService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileBasedCityService implements CityService {

    private final List<City> cities;

    public FileBasedCityService(ServiceProperties serviceProperties) throws IOException {
        try (InputStream inputStream = new ClassPathResource(serviceProperties.getCityFilename()).getInputStream()) {
            cities = new ObjectMapper().readValue(inputStream, new TypeReference<List<City>>() {
            });
        }
    }

    @Override
    public List<City> getCities() {
        return cities;
    }
}
