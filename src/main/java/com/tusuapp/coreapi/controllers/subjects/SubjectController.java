package com.tusuapp.coreapi.controllers.subjects;


import com.tusuapp.coreapi.models.Subject;
import com.tusuapp.coreapi.services.subjects.SubjectService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;


    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable Long id){
        return subjectService.getSubject(id);
    }

}
