package com.obaccelerator.portal.shared.session;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.id.UuidRepository;
import com.obaccelerator.portal.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class SessionService {

    private SessionRepository sessionRepository;
    private UuidRepository uuidRepository;

    public SessionService(SessionRepository sessionRepository, UuidRepository uuidRepository) {
        this.sessionRepository = sessionRepository;
        this.uuidRepository = uuidRepository;
    }

    public Session createSession(UUID portalUserId) {
        UUID uuid = uuidRepository.newId();
        sessionRepository.createSession(uuid, portalUserId);
        return findActiveSession(uuid.toString()).get();
    }

    public Optional<Session> findActiveSession(String sessionId) {
        return sessionRepository.findActiveSession(UUIDParser.fromString(sessionId));
    }

    public void updateSessionLastUsed(UUID sessionId) {
        sessionRepository.updateSessionLastUsed(sessionId);
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

}
