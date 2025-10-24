package com.tusuapp.coreapi.controllers.dropdown;

import com.tusuapp.coreapi.services.dropdowns.subjects.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CombinedController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/dropdowns")
@RequiredArgsConstructor
public class CombinedController {

    private final DropdownService dropdownService;

    @GetMapping
    public ResponseEntity<?> getDropdownItems(@RequestParam String types){
        return dropdownService.getItems(types);
    }


}
