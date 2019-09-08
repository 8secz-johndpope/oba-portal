package com.obaccelerator.portal.organization;

import com.obaccelerator.portal.gateway.organization.OrganizationObaGatewayService;
import com.obaccelerator.portal.registration.Registration;
import org.springframework.stereotype.Service;

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

    public Organization createFromRegistration(Registration registration) {
        UUID organizationId = organizationRepository.createOrganization(registration.getOrganizationName());
        organizationGatewayService.createOrganization(organizationId);
        return organizationRepository.findOrganization(organizationId).get();
    }

}
