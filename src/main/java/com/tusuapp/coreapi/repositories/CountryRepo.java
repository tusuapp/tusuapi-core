package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<Country,Integer> {
}
