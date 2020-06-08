package com.obaccelerator.portal.portaluser;

import com.obaccelerator.portal.id.UuidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.ORGANIZATION;

@Service
public class PortalUserService {

    private final PortalUserRepository portalUserRepository;
    private final UuidRepository uuidRepository;

    public PortalUserService(PortalUserRepository portalRepository, UuidRepository uuidRepository) {
        this.portalUserRepository = portalRepository;
        this.uuidRepository = uuidRepository;
    }

    public Optional<PortalUser> findByCognitoId(String cognitoId) {
        return portalUserRepository.findPortalUserByCognitoId(cognitoId);
    }

    public Optional<PortalUser> findById(UUID id, UUID organizationId) {
        return portalUserRepository.findById(id, organizationId);
    }

    @Transactional
    public PortalUser createPortalUserForCognitoUser(String cognitoUserId, UUID organizationId) {
        UUID uuid = uuidRepository.newId();
        portalUserRepository.createPortalUser(uuid, cognitoUserId, organizationId);
        portalUserRepository.createRoleLink(uuid, ORGANIZATION);
        return portalUserRepository.findPortalUserByCognitoId(cognitoUserId).get();
    }
}
