package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.response.PaymentHistory;
import group7.se1876.kcs_backend.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    public static PaymentHistory mapToPaymentHistory(Transaction transaction){
        return new PaymentHistory(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getUsername(),
                transaction.getBankCode()
        );
    }
}
