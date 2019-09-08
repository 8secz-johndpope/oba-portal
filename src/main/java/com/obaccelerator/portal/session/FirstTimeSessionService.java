package com.obaccelerator.portal.session;

import com.obaccelerator.portal.organization.Organization;
import com.obaccelerator.portal.organization.OrganizationService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.registration.Registration;
import com.obaccelerator.portal.registration.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;


/**
 * This service creates an organization based on a registration, a user and then links the registration to the
 * organization, so that we can see the registration was promoted to a registration.
 *
 * It does this when the user first logs in, so we know it is a valid user that is registered in Cognito.
 */
@Service
public class FirstTimeSessionService {

    private RegistrationService registrationService;
    private OrganizationService organizationService;
    private PortalUserService portalUserService;

    public FirstTimeSessionService(RegistrationService registrationService, OrganizationService organizationService, PortalUserService portalUserService) {
        this.registrationService = registrationService;
        this.organizationService = organizationService;
        this.portalUserService = portalUserService;
    }

    @Transactional
    public PortalUser promoteRegistrationToOrganizationWithUser(Map<String, Object> tokenClaims) {
        String cognitoId = tokenClaims.get("sub").toString();
        Optional<Registration> registrationOptional = registrationService.findRegistrationByCognitoId(cognitoId);
        if (registrationOptional.isPresent()) {
            Registration registration = registrationOptional.get();
            if (!registration.isPromotedToOrganisation()) {
                // Create the organization and the user
                Organization organization = organizationService.createFromRegistration(registration);
                return portalUserService.createPortalUser(cognitoId, organization.getId());
            } else {
                throw new RuntimeException("Found registration that is already linked to an organization for a first-time user. This should never happen.");
            }
        } else {
            throw new RuntimeException("Could not find registration for first-time user. This should never happen.");
        }
    }
}
