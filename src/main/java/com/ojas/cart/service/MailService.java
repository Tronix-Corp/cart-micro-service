package com.ojas.cart.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendMail(String to) throws Exception;
}
