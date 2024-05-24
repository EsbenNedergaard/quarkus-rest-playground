package com.madadipouya.quarkus.example.repository;

import com.madadipouya.quarkus.example.models.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Account> {
}
