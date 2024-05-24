package com.madadipouya.quarkus.example.service.impl;

import com.madadipouya.quarkus.example.entities.Account;
import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;
import com.madadipouya.quarkus.example.exception.ValidationException;
import com.madadipouya.quarkus.example.repository.AccountRepository;
import com.madadipouya.quarkus.example.service.AccountService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;

    @Inject
    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountById(long id) throws ResourceNotFoundException {
        return accountRepository.findByIdOptional(id).orElseThrow(() -> new ResourceNotFoundException("The resource does not exist"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.listAll();
    }

    @Transactional
    @Override
    public Account updateAccount(long id, Account account) throws ResourceNotFoundException {
        Account existingAccount = getAccountById(id);
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        accountRepository.persist(existingAccount);
        return existingAccount;
    }

    @Transactional
    @Override
    public Account createNewAccount(Account account) {
        account.setBalanceInDkk(0);
        accountRepository.persistAndFlush(account);
        return account;
    }

    @Transactional
    @Override
    public void deleteAccount(long id) throws ResourceNotFoundException {
        accountRepository.delete(getAccountById(id));
    }

    @Transactional
    @Override
    public Account deposit(long id, int amount) throws ResourceNotFoundException, ValidationException {
        if (amount < 0) {
            throw new ValidationException("Cannot deposit negative amount of money: " + amount);
        }
        Account existingAccount = getAccountById(id);
        existingAccount.setBalanceInDkk(existingAccount.getBalanceInDkk() + amount);
        accountRepository.persist(existingAccount);
        return existingAccount;
    }
}
