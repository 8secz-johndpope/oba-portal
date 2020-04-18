package com.obaccelerator.portal.session;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.id.UuidRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class SessionService {

    private SessionRepository sessionRepository;
    private UuidRepository uuidRepository;

    public SessionService(SessionRepository sessionRepository, UuidRepository uuidRepository) {
        this.sessionRepository = sessionRepository;
        this.uuidRepository = uuidRepository;
    }

    UUID createSession(UUID portalUserId) {
        UUID uuid = uuidRepository.newId();
        sessionRepository.createSession(uuid, portalUserId);
        return uuid;
    }

    Session findActiveSession(String sessionId) {
        Optional<Session> activeSession = sessionRepository.findActiveSession(UUIDParser.fromString(sessionId));
        if (activeSession.isPresent()) {
            sessionRepository.updateSessionLastUsed(UUIDParser.fromString(sessionId));
            return activeSession.get();
        } else {
            throw new EntityNotFoundException(Session.class, UUID.fromString(sessionId));
        }
    }

    public void updateSessionLastUsed(UUID sessionId) {
        sessionRepository.updateSessionLastUsed(sessionId);
    }

    void deleteSession(UUID sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

}
