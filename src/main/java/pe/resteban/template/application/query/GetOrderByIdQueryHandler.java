package pe.resteban.template.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.resteban.template.application.port.out.OrderRepository;
import pe.resteban.template.domain.model.Order;
import pe.resteban.template.shared.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class GetOrderByIdQueryHandler {

    private final OrderRepository orderRepository;

    public Order handle(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }
}
