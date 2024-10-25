package group7.se1876.kcs_backend.dto.request;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddOrderDetail {
    private int quantity;

    private double price;

    private String userName;

    private String address;

    private String phone;

    private LocalDate date;
}
