package Siddhesh.Banking_App.services;

import Siddhesh.Banking_App.config.SecurityConfig;
import Siddhesh.Banking_App.entities.User;
import Siddhesh.Banking_App.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private SecurityConfig config;

    public ResponseEntity<User> create(@RequestBody User acc){
        acc.setPassword(config.passwordEncoder().encode(acc.getPassword()));
        repository.save(acc);
        return new ResponseEntity<>(acc, HttpStatus.CREATED);
    }

    public List<User> getAllAccounts(){
        return repository.findAll();
    }

    public User getAccount(String username){
        return repository.findByUsername(username);
    }

    public synchronized ResponseEntity<?> withdraw(Long id, Double amt){
        User acc = repository.getReferenceById(id);
        if(acc != null){
            double curr = acc.getBalance();
            if(curr >= amt){
                curr -= amt;
                acc.setBalance(curr);
                repository.update(acc);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>("Insufficient Balance !!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public synchronized Boolean deposit(Long id, Double amt){
        User acc = repository.getReferenceById(id);
        if(acc != null){
            Double curr = acc.getBalance();
            curr += amt;
            acc.setBalance(curr);
            repository.update(acc);
            return true;
        }
        return false;
    }

    public ResponseEntity<?> delete(Long id) {
        repository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
