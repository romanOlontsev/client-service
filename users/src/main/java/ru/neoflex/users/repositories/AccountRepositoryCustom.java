package ru.neoflex.users.repositories;

import ru.neoflex.users.models.entities.Account;

import java.util.List;

public interface AccountRepositoryCustom {

    List<Account> findAccountByParams(String lastName, String firstName, String middleName,
                                      String phoneNumber, String email);
}
