package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimezoneRepo extends JpaRepository<Timezone, Long> {
}
