package com.madadipouya.quarkus.example.repository;

import com.madadipouya.quarkus.example.entities.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
}
