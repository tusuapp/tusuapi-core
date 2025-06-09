package com.tusuapp.coreapi.repositories;


import com.tusuapp.coreapi.models.CreditPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditPointRepo extends JpaRepository<CreditPoint,Long> {
}
