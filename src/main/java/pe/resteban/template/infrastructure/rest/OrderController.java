package pe.resteban.template.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.resteban.template.application.port.in.CreateOrderUseCase;
import pe.resteban.template.application.query.GetOrderByIdQueryHandler;
import pe.resteban.template.domain.commands.CreateOrderCommand;
import pe.resteban.template.domain.model.Order;
import pe.resteban.template.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdQueryHandler getOrderByIdQueryHandler;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getById(@PathVariable String id) {
        Order order = getOrderByIdQueryHandler.handle(id);
        return ResponseEntity.ok(ApiResponse.ok(order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody CreateOrderCommand command) {
        createOrderUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(null));
    }
}
