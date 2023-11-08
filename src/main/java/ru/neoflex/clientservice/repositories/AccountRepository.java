package ru.neoflex.clientservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.clientservice.models.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
}
