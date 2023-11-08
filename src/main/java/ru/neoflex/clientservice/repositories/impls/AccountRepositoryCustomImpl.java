package ru.neoflex.clientservice.repositories.impls;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import ru.neoflex.clientservice.models.entities.Account;
import ru.neoflex.clientservice.repositories.AccountRepositoryCustom;

import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> findAccountByParams(String lastName, String firstName, String middleName,
                                             String phoneNumber, String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> account = query.from(Account.class);

        List<Predicate> predicates = new ArrayList<>();
        if (lastName != null) {
            Path<String> lastnamePath = account.get("lastName");
            predicates.add(cb.equal(lastnamePath, lastName));
        }
        if (firstName != null) {
            Path<String> firstNamePath = account.get("firstName");
            predicates.add(cb.equal(firstNamePath, firstName));
        }
        if (middleName != null) {
            Path<String> middleNamePath = account.get("middleName");
            predicates.add(cb.equal(middleNamePath, middleName));
        }
        if (phoneNumber != null) {
            Path<String> phoneNumberPath = account.get("phoneNumber");
            predicates.add(cb.equal(phoneNumberPath, phoneNumber));
        }
        if (email != null) {
            Path<String> emailPath = account.get("email");
            predicates.add(cb.equal(emailPath, email));
        }
        query.select(account)
             .where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(query)
                            .getResultList();
    }
}
