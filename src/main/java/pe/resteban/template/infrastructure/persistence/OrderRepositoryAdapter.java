package pe.resteban.template.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.resteban.template.application.port.out.OrderRepository;
import pe.resteban.template.domain.model.Order;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    @Override
    public void save(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setStatus(order.getStatus());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaRepository.findById(id)
                .map(entity -> Order.builder()
                        .orderId(entity.getOrderId())
                        .status(entity.getStatus())
                        .build());
    }
}
