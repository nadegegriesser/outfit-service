package de.griesser.outfit.weatherserviceclient.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    private long id;
    private String name;
    private String country;
    private Coord coord;

}
