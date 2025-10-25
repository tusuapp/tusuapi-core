package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCaseAndProvider(String email, String provider);
    Optional<User> findByUsernameAndProvider(String username, String provider);

    Optional<User> findByPhone(String phone);

    List<User> findByRole(int role);

    @Query("""
        SELECT u FROM User u 
        WHERE (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchKey, '%')) 
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchKey, '%'))) 
           AND u.role = :role
    """)
    List<User> searchTutors(@Param("searchKey") String searchKey, @Param("role") Integer role);
}