package group7.se1876.kcs_backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionHistoryResponse {
    private Long id;
    private String username;
    private String details;
    private String date;
    private Double amount;
}
