package com.obaccelerator.portal.registration;

import com.obaccelerator.common.error.EntityNotFoundAfterInsertException;
import com.obaccelerator.portal.BotEvaluationResult;
import com.obaccelerator.portal.id.UuidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RegistrationService {

    private RegistrationRepository registrationRepository;
    private UuidRepository uuidRepository;

    public RegistrationService(final RegistrationRepository registrationRepository, UuidRepository uuidRepository) {
        this.registrationRepository = registrationRepository;
        this.uuidRepository = uuidRepository;
    }

    @Transactional
    public Registration createRegistration(RegistrationRequest registrationRequest, BotEvaluationResult botEvaluationResult) {
        isAlreadyRegistered(registrationRequest);
        UUID uuid = uuidRepository.newId();
        registrationRepository.createRegistration(uuid, registrationRequest.getCognitoUserId(),
                registrationRequest.getOrganizationName(), botEvaluationResult);
        return registrationRepository.findRegistration(uuid).orElseThrow(() -> new EntityNotFoundAfterInsertException(uuid));
    }

    @Transactional
    public Registration createRegistration(RegistrationRequest registrationRequest) {
        isAlreadyRegistered(registrationRequest);
        UUID uuid = uuidRepository.newId();
        registrationRepository.createRegistration(uuid, registrationRequest.getCognitoUserId(),
                registrationRequest.getOrganizationName());
        return registrationRepository.findRegistration(uuid).get();
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

    public void setOrganizatioIdForRegistration(UUID registrationId, UUID obaOrganizationId) {
        registrationRepository.setOrganizationId(registrationId, obaOrganizationId);
    }
}
