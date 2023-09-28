package com.egorza.spring.rest.crud.service;

import com.egorza.spring.rest.crud.model.Email;
import com.egorza.spring.rest.crud.repository.EmailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EmailsService {

    @Autowired
    EmailsRepository emailsRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${ENCRYPT_KEY}")
    private String key;

    public Email encryptAndSave(Email email) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            email.setEmail(Base64.getEncoder().encodeToString(cipher.doFinal(email.getEmail().getBytes())));
        } catch (Exception e) {
            // Handle exceptions
        }
        if (emailsRepository.findByEmail(email.getEmail()).isEmpty()) {
            emailsRepository.save(email);
        }
        sendNotificationEmail(email.getEmail());
        return email;
    }

    public Email decryptEmail(Email email) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            email.setEmail(new String(cipher.doFinal(Base64.getDecoder().decode(email.getEmail()))));
        } catch (Exception e) {
            // Handle exceptions
        }
        return email;
    }

    public void sendNotificationEmail(String newContactEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("egorzadorin04@gmail.com");
        message.setSubject("New Contact: " + newContactEmail);
        message.setText("Someone wants to contact you. Their email is " + newContactEmail);

        mailSender.send(message);
    }
}
