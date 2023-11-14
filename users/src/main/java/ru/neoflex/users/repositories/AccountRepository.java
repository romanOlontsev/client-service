package ru.neoflex.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.users.models.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
}
