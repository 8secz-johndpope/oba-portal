package com.obaccelerator.portal.session;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public UUID createSession(int portalUserId) {
        return sessionRepository.createSession(portalUserId);
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
