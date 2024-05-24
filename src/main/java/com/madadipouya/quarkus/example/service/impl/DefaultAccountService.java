package com.madadipouya.quarkus.example.service.impl;

import com.madadipouya.quarkus.example.models.Account;
import com.madadipouya.quarkus.example.exception.ResourceNotFoundException;
import com.madadipouya.quarkus.example.repository.UserRepository;
import com.madadipouya.quarkus.example.service.AccountService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DefaultAccountService implements AccountService {

    private final UserRepository userRepository;

    @Inject
    public DefaultAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Account getAccountById(long id) throws ResourceNotFoundException {
        return userRepository.findByIdOptional(id).orElseThrow(() -> new ResourceNotFoundException("There user doesn't exist"));
    }

    @Override
    public List<Account> getAllUsers() {
        return userRepository.listAll();
    }

    @Transactional
    @Override
    public Account updateAccount(long id, Account account) throws ResourceNotFoundException {
        Account existingAccount = getAccountById(id);
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        existingAccount.setAge(account.getAge());
        userRepository.persist(existingAccount);
        return existingAccount;
    }

    @Transactional
    @Override
    public Account saveAccount(Account account) {
        userRepository.persistAndFlush(account);
        return account;
    }

    @Transactional
    @Override
    public void deleteAccount(long id) throws ResourceNotFoundException {
        userRepository.delete(getAccountById(id));
    }
}
