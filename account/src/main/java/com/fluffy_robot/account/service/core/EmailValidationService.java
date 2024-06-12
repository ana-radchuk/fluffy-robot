package com.fluffy_robot.account.service.core;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidationService implements Predicate<String>  {
    @Override
    public boolean test(String s) {
        // TODO: implement regex for email validation
        return true;
    }
}
