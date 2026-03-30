package pe.resteban.template.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void builderShouldCreateOrderWithCorrectValues() {
        Order order = Order.builder()
                .orderId("ORD-001")
                .status("CREATED")
                .build();

        assertThat(order.getOrderId()).isEqualTo("ORD-001");
        assertThat(order.getStatus()).isEqualTo("CREATED");
    }

    @Test
    void orderIdShouldNotBeNullAfterConstruction() {
        Order order = Order.builder()
                .orderId("ORD-001")
                .status("CREATED")
                .build();

        assertThat(order.getOrderId()).isNotNull();
    }
}
