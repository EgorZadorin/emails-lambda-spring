package spring.rest.crud.service;

import spring.rest.crud.exceptions.NotFoundException;
import spring.rest.crud.model.Email;
import spring.rest.crud.repository.EmailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.util.Base64;


@Service
public class EmailsService {

    @Autowired
    EmailsRepository emailsRepository;

    @Value("${ENCRYPT_KEY}")
    private String key;

    @Value("${EMAIL_SENDER}")
    private String emailSender;

    @Value("${MY_EMAIL}")
    private String myEmail;

    @Transactional
    public Email encryptAndSaveOrDelete(Email email, Boolean delete) throws NotFoundException {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            email.setEmail(Base64.getEncoder().encodeToString(cipher.doFinal(email.getEmail().getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (delete) {
            emailsRepository.deleteByEmail(email.getEmail());
        } else if (emailsRepository.findByEmail(email.getEmail()).isEmpty()) {
            emailsRepository.save(email);
            sendNotificationEmail(decryptEmail(email).getEmail());
        }
        return decryptEmail(email);
    }

    public Email decryptEmail(Email email) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            email.setEmail(new String(cipher.doFinal(Base64.getDecoder().decode(email.getEmail()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    public void sendNotificationEmail(String newContactEmail) {
        SesClient ses = SesClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(emailSender)
                .destination(Destination.builder()
                        .toAddresses(myEmail)
                        .build())
                .message(Message.builder()
                        .subject(Content.builder().data("New Contact Email").charset("UTF-8").build())
                        .body(Body.builder()
                                .text(Content.builder().data("The person with the following email:\n" +
                                        newContactEmail + "\nwants to contact you!").charset("UTF-8").build())
                                .build())
                        .build())
                .build();

        ses.sendEmail(request);
    }
}
