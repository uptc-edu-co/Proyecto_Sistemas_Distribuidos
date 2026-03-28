package co.edu.uptc.shared.messaging.dto;

import java.time.Instant;

public class EventDTO {
    private Instant timestamp;
    private String serviceOrigin;
    private String action;
    private Object data;

    public EventDTO() {}

    public EventDTO(Instant timestamp, String serviceOrigin, String action, Object data) {
        this.timestamp = timestamp;
        this.serviceOrigin = serviceOrigin;
        this.action = action;
        this.data = data;
    }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getServiceOrigin() { return serviceOrigin; }
    public void setServiceOrigin(String serviceOrigin) { this.serviceOrigin = serviceOrigin; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}