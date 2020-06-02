package com.obaccelerator.portal.session;

import com.obaccelerator.portal.organization.ObaOrganizationResponse;
import com.obaccelerator.portal.organization.OrganizationObaGatewayService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.registration.Registration;
import com.obaccelerator.portal.registration.RegistrationService;
import com.obaccelerator.portal.token.TokenProviderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;


/**
 * This service creates an organization in OBA based on a registration in OBA Portal, a user and then links the
 * registration to the organization, so that we can later still see the registration was promoted to an organization.
 *
 * A registration is promoted to an organization when the user first logs in, so we know a valid user is registered
 * in Cognito.
 */
@Service
public class FirstTimeSessionService {

    private final RegistrationService registrationService;
    private final OrganizationObaGatewayService organizationObaGatewayService;
    private final PortalUserService portalUserService;

    public FirstTimeSessionService(RegistrationService registrationService,
                                   OrganizationObaGatewayService organizationObaGatewayService,
                                   PortalUserService portalUserService) {
        this.registrationService = registrationService;
        this.organizationObaGatewayService = organizationObaGatewayService;
        this.portalUserService = portalUserService;
    }

    @Transactional
    public PortalUser promoteRegistrationToOrganizationWithUser(Map<String, Object> tokenClaims) {
        String cognitoId = tokenClaims.get("sub").toString();
        // Get the registration
        Optional<Registration> registrationOptional = registrationService.findRegistrationByCognitoId(cognitoId);
        if (registrationOptional.isPresent()) {
            Registration registration = registrationOptional.get();
            if (!registration.isPromotedToOrganizationWithId()) {
                // Create the organization
                ObaOrganizationResponse organization = organizationObaGatewayService.createOrganizationFromRegistration(registration);
                // Update the registration with the organization id, so we can track which registration turned into
                // organizations
                registrationService.setOrganizatioIdForRegistration(registration.getId(), organization.getId());
                // Create the portal user
                return portalUserService.createPortalUserForCognitoUser(cognitoId, organization.getId());
            } else {
                throw new RuntimeException("Found registration that is already linked to an organization for a " +
                        "first-time user. This should never happen.");
            }
        } else {
            throw new RuntimeException("Could not find registration for first-time user. This can only happen if a " +
                    "user was successfully registered in Cognito, but no associated registration was created in OBA.");
        }
    }
}
