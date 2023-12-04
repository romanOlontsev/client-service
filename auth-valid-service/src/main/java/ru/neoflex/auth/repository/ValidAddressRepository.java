package ru.neoflex.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.auth.models.entity.ValidAddress;

import java.util.Optional;

public interface ValidAddressRepository extends JpaRepository<ValidAddress, Long>, HibernateRepository<ValidAddress> {

    Optional<ValidAddress> findValidAddressByAddressName(String addressName);
}
