package de.griesser.outfit.web.api;

import java.util.SortedSet;

public interface CountryController {

    SortedSet<Country> getCountriesSortedByCountryCode();

}
