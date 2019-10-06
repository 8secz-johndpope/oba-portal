package com.obaccelerator.portal.organization;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.portal.gateway.organization.OrganizationObaGatewayService;
import com.obaccelerator.portal.id.UuidRepository;
import com.obaccelerator.portal.registration.Registration;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private OrganizationObaGatewayService organizationObaGatewayService;
    private OrganizationRepository organizationRepository;
    private UuidRepository uuidRepository;

    public OrganizationService(OrganizationObaGatewayService organizationObaGatewayService,
                               OrganizationRepository organizationRepository,
                               UuidRepository uuidRepository) {
        this.organizationObaGatewayService = organizationObaGatewayService;
        this.organizationRepository = organizationRepository;
        this.uuidRepository = uuidRepository;
    }

    // TODO: architecture : this first creates the organization in the portal, then in OBA.
    //  This puts the portal in charge of id creation. Also, if creation of the organization in OBA fails, we must clean up
    //  in order to prevent useless organization in the database. The organization should first be created in OBA. Only if that
    //  succeeds it must be created in OBA Portal.
    // @Transactional
    // TODO: Organization is automatically rolled back, even without Transactional annotation. Why?
    public Organization createFromRegistration(Registration registration) {
        UUID uuid = uuidRepository.newId();
        organizationRepository.createOrganization(uuid, registration.getOrganizationName());
        organizationObaGatewayService.createOrganization(uuid);
        return organizationRepository.findOrganization(uuid);
    }

    public Organization findOrganization(UUID id) {
        Organization organization = organizationRepository.findOrganization(id);
        if (organization == null) {
            throw new EntityNotFoundException(Organization.class);
        }
        return organization;
    }

}
