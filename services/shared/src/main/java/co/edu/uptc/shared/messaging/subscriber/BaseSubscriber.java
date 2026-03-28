package co.edu.uptc.shared.messaging.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.edu.uptc.shared.messaging.dto.EventDTO;

public abstract class BaseSubscriber {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected void process(EventDTO event) {
        try {
            onEvent(event);
        } catch (RuntimeException ex) {
            log.error("Error processing event action={} origin={}", event.getAction(), event.getServiceOrigin(), ex);
            throw ex;
        }
    }

    protected abstract void onEvent(EventDTO event);
}