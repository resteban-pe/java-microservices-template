package pe.resteban.template.application.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.resteban.template.application.port.out.OrderRepository;
import pe.resteban.template.domain.commands.CreateOrderCommand;
import pe.resteban.template.domain.model.Order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateOrderCommandHandlerTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderCommandHandler handler;

    @Test
    void shouldCallSaveExactlyOnce() {
        CreateOrderCommand command = new CreateOrderCommand("ORD-001", "Test order");

        handler.execute(command);

        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
