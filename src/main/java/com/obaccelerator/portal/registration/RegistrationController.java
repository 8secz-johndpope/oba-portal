package com.obaccelerator.portal.registration;

import com.obaccelerator.portal.BotEvaluationResult;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
public class RegistrationController {

    private RegistrationService registrationService;

    public RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registrations")
    public Registration postRegistration(@RequestBody @Valid RegistrationRequest registrationRequest, HttpServletRequest request) {
        Registration registration = null;
        if(registrationRequest.isLikelyABotRequest()) {
            try {
                String requestBody = IOUtils.toString(request.getReader());
                BotEvaluationResult botEvaluationResult = new BotEvaluationResult(true, requestBody);
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
