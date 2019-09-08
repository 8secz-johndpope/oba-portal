package com.obaccelerator.portal.portaluser;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PortalUserService {

    private PortalUserRepository portalRepository;

    public PortalUserService(PortalUserRepository portalRepository) {
        this.portalRepository = portalRepository;
    }

    public Optional<PortalUser> findByCognitoId(String cognitoId) {
        return portalRepository.findPortalUserByCognitoId(cognitoId);
    }

    public PortalUser createPortalUser(String cognitoUserId, UUID organizationId) {
        portalRepository.createPortalUser(cognitoUserId, organizationId);
        return portalRepository.findPortalUserByCognitoId(cognitoUserId).get();
    }
}
