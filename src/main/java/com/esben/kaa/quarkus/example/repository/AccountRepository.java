package com.esben.kaa.quarkus.example.repository;

import com.esben.kaa.quarkus.example.entities.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
}
