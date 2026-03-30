package pe.resteban.template.application.port.out;

import pe.resteban.template.domain.model.Order;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String id);
}
