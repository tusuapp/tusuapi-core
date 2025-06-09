package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.TutorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorDetailRepo extends JpaRepository<TutorDetails,Long> {

}
