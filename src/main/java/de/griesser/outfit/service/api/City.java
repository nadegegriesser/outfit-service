package de.griesser.outfit.service.api;

import lombok.Data;

@Data
public class City {

    private long id;
    private String name;
    private String country;
    private Coord coord;

}
