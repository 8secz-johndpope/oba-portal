package com.obaccelerator.portal.shared.session;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.id.UuidRepository;
import com.obaccelerator.portal.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;


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
        return findActiveSession(uuid.toString());
    }

    public Session findActiveSession(String sessionId) {
        if (isBlank(sessionId)) {
            throw new NoSessionException();
        }

        Optional<Session> activeSession = sessionRepository.findActiveSession(UUIDParser.fromString(sessionId));
        if (activeSession.isPresent()) {
            sessionRepository.updateSessionLastUsed(UUIDParser.fromString(sessionId));
            return activeSession.get();
        }
        throw new NoSessionException();
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

}
