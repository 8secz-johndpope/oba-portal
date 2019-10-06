package com.obaccelerator.portal.session;

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

    public UUID createSession(UUID portalUserId) {
        UUID uuid = uuidRepository.newId();
        sessionRepository.createSession(uuid, portalUserId);
        return uuid;
    }

    public void updateSessionLastUsed(UUID sessionId) {
        sessionRepository.updateSessionLastUsed(sessionId);
    }

    public Optional<Session> findValidSession(UUID sessionId) {
        return sessionRepository.findValidSession(sessionId);
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

}
