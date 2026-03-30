package pe.resteban.template.domain.events;

import java.time.Instant;

public record OrderCreatedEvent(String orderId, Instant timestamp) {
}
