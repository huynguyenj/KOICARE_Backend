package group7.se1876.kcs_backend.dto.request;


import group7.se1876.kcs_backend.entity.Product;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddOrderDetail {

    private String userName;

    private String address;

    private String phone;

    private List<OrderRequest> order;
}
