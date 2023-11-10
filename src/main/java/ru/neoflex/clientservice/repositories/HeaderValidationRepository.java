package ru.neoflex.clientservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.neoflex.clientservice.models.entities.HeaderValidation;

import java.util.List;
import java.util.Optional;

public interface HeaderValidationRepository extends JpaRepository<HeaderValidation, Long> {

    @Query("SELECT h.requiredFields FROM HeaderValidation h WHERE h.headerName=:header")
    List<String> findRequiredFieldsByHeader(@Param("header") String header);

    @Query("SELECT h.id FROM HeaderValidation h WHERE h.headerName=:header")
    Optional<Long> findHeaderIdByName(String header);
}
