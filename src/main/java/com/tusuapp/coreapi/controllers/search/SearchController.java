package com.tusuapp.coreapi.controllers.search;

import com.tusuapp.coreapi.services.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/tutors")
    public ResponseEntity<?> searchTutors(@RequestParam String key){
        return searchService.searchTutor(key);
    }

}
