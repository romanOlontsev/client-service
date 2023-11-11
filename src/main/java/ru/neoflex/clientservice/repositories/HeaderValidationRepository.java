package ru.neoflex.clientservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.neoflex.clientservice.models.entities.HeaderValidation;

import java.util.List;
import java.util.Optional;

public interface HeaderValidationRepository extends JpaRepository<HeaderValidation, Long> {

    @Query("SELECT h FROM HeaderValidation h JOIN FETCH h.requiredFields")
    List<HeaderValidation> findAllHeaderValidation();

    @Query("SELECT h FROM HeaderValidation h JOIN FETCH h.requiredFields WHERE h.headerName=:header")
    Optional<HeaderValidation> findHeaderValidationByHeader(@Param("header") String header);

    @Modifying
    @Query("DELETE FROM HeaderValidation h WHERE h.headerName=:header")
    void deleteByHeader(@Param("header") String header);
}
