package de.griesser.outfit.weatherserviceclient.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.griesser.outfit.weatherserviceclient.api.City;
import de.griesser.outfit.weatherserviceclient.api.CityService;
import de.griesser.outfit.weatherserviceclient.config.ServiceProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileBasedCityService implements CityService {

    private final List<City> cities;

    FileBasedCityService(ServiceProperties serviceProperties) throws IOException {
        List<City> cities;
        try (InputStream inputStream = new ClassPathResource(serviceProperties.getCityFilename()).getInputStream()) {
            cities = new ObjectMapper().readValue(inputStream, new TypeReference<List<City>>() {
            });
        }
        this.cities = cities
                .stream()
                .filter(city -> !StringUtils.isEmpty(city.getCountry()))
                .collect(Collectors.toList());
    }

    @Override
    public List<City> getCities() {
        return cities;
    }
}
