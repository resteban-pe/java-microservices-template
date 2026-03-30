package pe.resteban.template.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Order {

    private String orderId;
    private String status;
}
