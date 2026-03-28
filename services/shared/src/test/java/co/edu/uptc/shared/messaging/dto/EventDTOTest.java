package co.edu.uptc.shared.messaging.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventDTOTest {

    @Test
    void testNoArgsConstructor() {
        EventDTO dto = new EventDTO();
        assertNull(dto.getTimestamp());
        assertNull(dto.getServiceOrigin());
        assertNull(dto.getAction());
        assertNull(dto.getData());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Instant now = Instant.now();
        EventDTO dto = new EventDTO(now, "auth-service", "CREATE", "test-data");

        assertEquals(now, dto.getTimestamp());
        assertEquals("auth-service", dto.getServiceOrigin());
        assertEquals("CREATE", dto.getAction());
        assertEquals("test-data", dto.getData());
    }

    @Test
    void testSetters() {
        EventDTO dto = new EventDTO();
        Instant now = Instant.now();

        dto.setTimestamp(now);
        dto.setServiceOrigin("user-service");
        dto.setAction("UPDATE");
        dto.setData(123);

        assertEquals(now, dto.getTimestamp());
        assertEquals("user-service", dto.getServiceOrigin());
        assertEquals("UPDATE", dto.getAction());
        assertEquals(123, dto.getData());
    }
}
