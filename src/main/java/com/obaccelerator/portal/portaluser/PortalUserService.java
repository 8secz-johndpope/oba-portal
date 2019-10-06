package com.obaccelerator.portal.portaluser;

import com.obaccelerator.portal.id.UuidRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PortalUserService {

    private PortalUserRepository portalRepository;
    private UuidRepository uuidRepository;

    public PortalUserService(PortalUserRepository portalRepository, UuidRepository uuidRepository) {
        this.portalRepository = portalRepository;
        this.uuidRepository = uuidRepository;
    }

    public Optional<PortalUser> findByCognitoId(String cognitoId) {
        return portalRepository.findPortalUserByCognitoId(cognitoId);
    }

    public PortalUser createPortalUserForCognitoUser(String cognitoUserId, UUID organizationId) {
        UUID uuid = uuidRepository.newId();
        portalRepository.createPortalUser(uuid, cognitoUserId, organizationId);
        return portalRepository.findPortalUserByCognitoId(cognitoUserId).get();
    }
}
