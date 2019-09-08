package com.obaccelerator.portal.registration;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.portal.BotEvaluationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
        isAlreadyRegistered(registrationRequest);
        UUID id = registrationRepository.createRegistration(registrationRequest.getCognitoUserId(), registrationRequest.getOrganizationName(), botEvaluationResult);
        return registrationRepository.findRegistration(id).orElseThrow(() -> new EntityNotFoundException(Registration.class));
    }

    @Transactional
    public Registration createRegistration(RegistrationRequest registrationRequest) {
        isAlreadyRegistered(registrationRequest);
        UUID id = registrationRepository.createRegistration(registrationRequest.getCognitoUserId(), registrationRequest.getOrganizationName());
        return registrationRepository.findRegistration(id).get();
    }

    public Optional<Registration> findRegistrationByCognitoId(String cognitoId) {
        return registrationRepository.findRegistrationByCognitoId(cognitoId);
    }

    private void isAlreadyRegistered(RegistrationRequest registrationRequest) {
        Optional<Registration> registrationByEmailOptional = registrationRepository.findRegistrationByCognitoId(registrationRequest.getEmail());
        if (registrationByEmailOptional.isPresent()) {
            throw new RegistrationAlreadyExistsException();
        }
    }


}
