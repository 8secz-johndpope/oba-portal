package com.obaccelerator.portal.organization;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.portal.gateway.organization.OrganizationObaGatewayService;
import com.obaccelerator.portal.registration.Registration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private OrganizationObaGatewayService organizationGatewayService;
    private OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationObaGatewayService organizationGatewayService,
                               OrganizationRepository organizationRepository) {
        this.organizationGatewayService = organizationGatewayService;
        this.organizationRepository = organizationRepository;
    }

    // TODO: architecture : this first creates the organization in the portal, then in OBA.
    //  This puts the portal in charge of id creation. Also, if creation of the organization in OBA fails, we must clean up
    //  in order to prevent useless organization in the database. The organization should first be created in OBA. Only if that
    //  succeeds it must be created in OBA Portal.
    // @Transactional
    // TODO: Organization is automatically rolled back. Why?
    public Organization createFromRegistration(Registration registration) {
        UUID organizationId = organizationRepository.createOrganization(registration.getOrganizationName());
        organizationGatewayService.createOrganization(organizationId);
        return organizationRepository.findOrganization(organizationId).get();
    }

    public Organization findOrganization(UUID id) {
        Optional<Organization> organizationOptional = organizationRepository.findOrganization(id);
        if (!organizationOptional.isPresent()) {
            throw new EntityNotFoundException(Organization.class);
        }
        return organizationOptional.get();
    }

}
