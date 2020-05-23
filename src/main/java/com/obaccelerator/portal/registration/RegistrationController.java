package com.obaccelerator.portal.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaccelerator.portal.BotEvaluationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/registrations")
    public Registration postRegistration(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Registration registration;
        if(registrationRequest.isLikelyABotRequest()) {
            try {
                BotEvaluationResult botEvaluationResult = new BotEvaluationResult(true, objectMapper.writeValueAsString(registrationRequest));
                registration = registrationService.createRegistration(registrationRequest, botEvaluationResult);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            registration = registrationService.createRegistration(registrationRequest);
        }
        return registration;
    }
}
