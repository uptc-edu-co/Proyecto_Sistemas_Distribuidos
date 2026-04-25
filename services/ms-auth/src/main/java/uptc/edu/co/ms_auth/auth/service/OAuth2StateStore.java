package uptc.edu.co.ms_auth.auth.service;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OAuth2StateStore {

    private final Map<String, Instant> states = new ConcurrentHashMap<>();
    private final Duration ttl = Duration.ofMinutes(10);

    public String createState() {
        String state = UUID.randomUUID().toString();
        states.put(state, Instant.now().plus(ttl));
        return state;
    }

    public boolean consumeState(String state) {
        if (state == null || state.isBlank()) {
            return false;
        }

        Instant expiresAt = states.remove(state);
        if (expiresAt == null) {
            return false;
        }

        return Instant.now().isBefore(expiresAt);
    }
}
