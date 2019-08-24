package com.obaccelerator.portal.registration;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaException;
import com.obaccelerator.portal.BotEvaluationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Service
public class RegistrationService {

    private RegistrationRepository registrationRepository;

    public RegistrationService(final RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public Registration createRegistration(RegistrationRequest registrationRequest, BotEvaluationResult botEvaluationResult) {
        UUID id = registrationRepository.createRegistration(registrationRequest.getFirstName(),
                registrationRequest.getLastName(), registrationRequest.getEmail(), registrationRequest.getCompanyName(), botEvaluationResult);
        return registrationRepository.findRegistration(id).orElseThrow(() -> new EntityNotFoundException(Registration.class));
    }

    public Registration createRegistration(RegistrationRequest registrationRequest) {
        UUID id = registrationRepository.createRegistration(registrationRequest.getFirstName(),
                registrationRequest.getLastName(), registrationRequest.getEmail(), registrationRequest.getCompanyName());
        Optional<Registration> registration = registrationRepository.findRegistration(id);
        if(registration.isPresent()) {
            return  registration.get();
        }
        throw new EntityNotFoundException(Registration.class);
    }

    @Async
    public void sendRegistrationEmail(Registration registration) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("AKIA2ZQDIZRNNBQZWRH6", "BHTqh1XR1nkIZdJh3EQiA8UT/n5UhLzpM2Mr+Fy++JwJ");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("obaccelerator@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(registration.getEmail()));
            message.setSubject("Your OBA registration");

            String msg = "Welcome to OBA. PLease go <a href='https://wwww.google.com'>here</a> to set your password</a>'";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new ObaException(ObaError.PORTAL_COULD_NOT_SEND_REGISTRATION_EMAIL);
        }


    }


}
