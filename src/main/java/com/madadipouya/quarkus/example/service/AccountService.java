package com.madadipouya.quarkus.example.service;

import com.madadipouya.quarkus.example.entities.Account;
import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;

import java.util.List;

public interface AccountService {

    Account getAccountById(long id) throws ResourceNotFoundException;

    List<Account> getAllAccounts();

    Account updateAccount(long id, Account account) throws ResourceNotFoundException;

    Account createNewAccount(Account account);

    void deleteAccount(long id) throws ResourceNotFoundException;
}
