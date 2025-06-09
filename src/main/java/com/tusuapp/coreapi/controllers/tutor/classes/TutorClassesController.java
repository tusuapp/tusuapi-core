package com.tusuapp.coreapi.controllers.tutor.classes;


import com.tusuapp.coreapi.services.user.classes.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tutor/classes")
public class TutorClassesController {

    @Autowired
    private ClassesService classesService;

    @GetMapping
    public ResponseEntity<?> getClasses(@RequestParam("types") String types){
        return  ResponseEntity.ok(classesService.getMyClasses(types));
    }
//
//    @PutMapping
//    public ResponseEntity<?> rejectClass(){
//        return  ResponseEntity.ok(classesService.getMyClasses(types));
//    }

}
