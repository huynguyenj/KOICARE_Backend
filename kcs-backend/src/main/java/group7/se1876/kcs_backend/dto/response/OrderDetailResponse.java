package group7.se1876.kcs_backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailResponse {

    private int orderId;

    private int quantity;

    private double price;

    private String userName;

    private String address;

    private String phone;

    private Date date;

    private String productName;
}
