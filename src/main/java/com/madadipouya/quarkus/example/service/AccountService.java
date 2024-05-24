package com.madadipouya.quarkus.example.service;

import com.madadipouya.quarkus.example.dtos.TransferRequestDto;
import com.madadipouya.quarkus.example.entities.Account;
import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;
import com.madadipouya.quarkus.example.exception.ValidationException;

import java.util.List;

public interface AccountService {

    Account getAccountById(long id) throws ResourceNotFoundException;

    List<Account> getAllAccounts();

    Account updateAccount(long id, Account account) throws ResourceNotFoundException;

    Account createNewAccount(Account account);

    void deleteAccount(long id) throws ResourceNotFoundException;

    Account deposit(long id, int amount) throws ResourceNotFoundException, ValidationException;

    void transferMoney(TransferRequestDto transferRequestDto) throws ResourceNotFoundException, ValidationException;
}
