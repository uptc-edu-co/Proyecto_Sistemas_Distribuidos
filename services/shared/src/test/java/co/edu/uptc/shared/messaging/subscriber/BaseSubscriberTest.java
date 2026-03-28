package co.edu.uptc.shared.messaging.subscriber;

import co.edu.uptc.shared.messaging.dto.EventDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseSubscriberTest {

    private static class TestSubscriber extends BaseSubscriber {
        private boolean eventProcessed = false;
        private boolean throwException = false;

        @Override
        protected void onEvent(EventDTO event) {
            if (throwException) {
                throw new RuntimeException("Simulated error");
            }
            eventProcessed = true;
        }

        public void callProcess(EventDTO event) {
            super.process(event);
        }

        public boolean isEventProcessed() {
            return eventProcessed;
        }

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }
    }

    @Test
    void testProcessSuccessful() {
        TestSubscriber subscriber = new TestSubscriber();
        EventDTO event = new EventDTO();
        event.setAction("TEST_SUCCESS");
        
        subscriber.callProcess(event);

        assertTrue(subscriber.isEventProcessed());
    }

    @Test
    void testProcessThrowsException() {
        TestSubscriber subscriber = new TestSubscriber();
        subscriber.setThrowException(true);
        
        EventDTO event = new EventDTO();
        event.setAction("TEST_ERROR");
        event.setServiceOrigin("test-service");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriber.callProcess(event);
        });

        assertEquals("Simulated error", exception.getMessage());
    }
}
