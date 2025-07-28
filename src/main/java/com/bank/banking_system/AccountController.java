package com.bank.banking_system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/balance")
    public Long calculateAccountBalance() {
        return 421L;
    }

}
