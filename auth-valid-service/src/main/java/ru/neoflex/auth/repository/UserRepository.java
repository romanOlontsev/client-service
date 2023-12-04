package ru.neoflex.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.neoflex.auth.models.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, HibernateRepository<User> {

    @Query("SELECT u FROM User u WHERE u.login=:login")
    Optional<User> findUserByLogin(@Param("login") String login);
}
