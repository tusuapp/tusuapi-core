package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
