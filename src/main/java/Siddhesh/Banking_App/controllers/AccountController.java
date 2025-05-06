package Siddhesh.Banking_App.controllers;

import Siddhesh.Banking_App.entities.User;
import Siddhesh.Banking_App.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class AccountController {

    @Autowired
    AccountService service ;

    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody User acc){
        return service.create(acc);
    }

    @GetMapping("/admin")
    public List<User> getAll(){
        return service.getAllAccounts();
    }

    @GetMapping("/user")
    public User getAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return service.getAccount(name);
    }

    @PutMapping("/user/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam Double amt){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User account = service.getAccount(name);
        return service.withdraw(account.getId(), amt);
    }

    @PutMapping("/user/deposit")
    public ResponseEntity<?> deposiit(@RequestParam Double amt){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User account = service.getAccount(name);
        Boolean deposit = service.deposit(account.getId(), amt);
        if(deposit) return new ResponseEntity<>(amt,HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAcc(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User account = service.getAccount(name);
        return service.delete(account.getId());
    }

}
