package com.obaccelerator.portal.bank;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class BankController {

    public List<Bank> findAll() {
        return null;
    }
}
