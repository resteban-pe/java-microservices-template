package pe.resteban.template.application.port.in;

import pe.resteban.template.domain.commands.CreateOrderCommand;

public interface CreateOrderUseCase {

    void execute(CreateOrderCommand command);
}
