package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c WHERE c.type IN :types ORDER BY c.position ASC")
    List<Category> findAllByAnyType(List<String> types);
}
