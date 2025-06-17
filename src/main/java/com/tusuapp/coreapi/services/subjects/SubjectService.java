package com.tusuapp.coreapi.services.subjects;


import com.tusuapp.coreapi.models.Subject;
import com.tusuapp.coreapi.repositories.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;


    public ResponseEntity<Subject> getSubject(Long id){
        Subject subject = subjectRepo.findById(id).orElseThrow(()->new IllegalArgumentException("No subject found for id"));
        return ResponseEntity.ok(subject);
    }


}
