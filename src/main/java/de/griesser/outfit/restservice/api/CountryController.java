package de.griesser.outfit.restservice.api;

import java.util.SortedSet;

public interface CountryController {

    SortedSet<Country> getCountriesSortedByCountryCode();

}
