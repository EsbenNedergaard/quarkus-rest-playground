package com.madadipouya.quarkus.example.service;

import com.madadipouya.quarkus.example.models.Account;
import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;

import java.util.List;

public interface AccountService {

    Account getAccountById(long id) throws ResourceNotFoundException;

    List<Account> getAllUsers();

    Account updateAccount(long id, Account account) throws ResourceNotFoundException;

    Account saveAccount(Account account);

    void deleteAccount(long id) throws ResourceNotFoundException;
}
