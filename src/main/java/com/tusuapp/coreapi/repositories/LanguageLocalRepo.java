package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.LanguageLocale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageLocalRepo extends JpaRepository<LanguageLocale, Long> {
}
