package com.tusuapp.coreapi.services.dropdowns.subjects;

import com.tusuapp.coreapi.models.Category;
import com.tusuapp.coreapi.models.Country;
import com.tusuapp.coreapi.models.LanguageLocale;
import com.tusuapp.coreapi.models.Timezone;
import com.tusuapp.coreapi.models.dtos.CountryDto;
import com.tusuapp.coreapi.models.dtos.dropdowns.CategoryDto;
import com.tusuapp.coreapi.repositories.CategoryRepo;
import com.tusuapp.coreapi.repositories.CountryRepo;
import com.tusuapp.coreapi.repositories.LanguageLocalRepo;
import com.tusuapp.coreapi.repositories.TimezoneRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * DropdownService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class DropdownService {

    private final CountryRepo countryRepo;
    private final CategoryRepo categoryRepo;
    private final TimezoneRepo timezoneRepo;
    private final LanguageLocalRepo languageRepo;

    public static final String LANGUAGES = "languages";
    public static final String COUNTRIES= "countries";
    public static final String TIMEZONES = "timezone";
    public static final String DISCIPLINE = "discipline";
    public static final String SUBJECT = "subject";
    public static final String EDUCATION = "education";


    public ResponseEntity<?> getItems(String typesCombined) {
        JSONObject response = new JSONObject();
        List<String> types = Arrays.stream(typesCombined.split(",")).toList();
        if (types.contains(COUNTRIES)) {
            List<Country> countries = countryRepo.findAll();
            List<CountryDto> countryDtos = countries.stream().map(CountryDto::fromCountry).toList();
            response.put("countries", countryDtos);
        }
        if (types.contains(EDUCATION) || types.contains(SUBJECT) || types.contains(DISCIPLINE)) {
            List<Category> categories = categoryRepo.findAllByAnyType(types);
            List<CategoryDto> categoryDtos = categories.stream().map(CategoryDto::fromCategory).toList();
            response.put(DISCIPLINE, categoryDtos.stream().filter((c) -> c.getType().equals(DISCIPLINE)).toList());
            response.put(SUBJECT, categoryDtos.stream().filter((c) -> c.getType().equals(SUBJECT)).toList());
            response.put(EDUCATION, categoryDtos.stream().filter((c) -> c.getType().equals(EDUCATION)).toList());
        }
        if(types.contains(TIMEZONES)){
            List<Timezone> timezones = timezoneRepo.findAll();
            List<String> zoneOnlye = timezones.stream().map(Timezone::getTimezone).toList();
            response.put("timezones", zoneOnlye);
        }

        if(types.contains(LANGUAGES)){
            List<LanguageLocale> languages = languageRepo.findAll();
//            List<String> zoneOnlye = timezones.stream().map(Timezone::getTimezone).toList();
            response.put(LANGUAGES, languages);
        }
        return ResponseEntity.ok(response.toMap());
    }

}
