package pe.resteban.template.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.resteban.template.application.port.in.CreateOrderUseCase;
import pe.resteban.template.application.port.out.OrderRepository;
import pe.resteban.template.domain.commands.CreateOrderCommand;
import pe.resteban.template.domain.model.Order;

@Service
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements CreateOrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public void execute(CreateOrderCommand command) {
        Order order = Order.builder()
                .orderId(command.orderId())
                .status("CREATED")
                .build();
        orderRepository.save(order);
    }
}
