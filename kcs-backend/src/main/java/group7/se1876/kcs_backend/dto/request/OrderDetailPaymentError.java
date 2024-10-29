package group7.se1876.kcs_backend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailPaymentError {

    private List<OrderRequest> order;

    private List<Integer> orderId;

}
