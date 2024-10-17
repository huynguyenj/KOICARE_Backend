package group7.se1876.kcs_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // No using 1010 it already used

    INVALID_KEY(1000,"Invalid message key",HttpStatus.BAD_REQUEST),
    UNCATAGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed(please check your username or your email)",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1002,"User password must at least 8 character.",HttpStatus.BAD_REQUEST),
    INVALID_USERID(1003,"This userID is not existed",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User invalid(please check your username or your password)",HttpStatus.NOT_FOUND),
    UNAUTHENDICATED (1005,"Unauthendicated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006,"You do not have permission for this function",HttpStatus.FORBIDDEN),
    INVALID_INFOMATION(1007,"You need to check your information that it do not duplicated",HttpStatus.BAD_REQUEST),
    DATA_NOT_EXISTED(1008,"Your data is not existed",HttpStatus.BAD_REQUEST),
    DELETE_FAIL(1009,"Delete fail",HttpStatus.BAD_REQUEST),
    UPDATE_FAIL(1010,"Update fail",HttpStatus.BAD_REQUEST),
    ORDER_DETAIL_NOT_FOUND(1011, "Order detail not found", HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND(1012, "Item not found", HttpStatus.NOT_FOUND),
    ITEM_EXISTED(1013, "Item existed", HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK(1014, "Out of stock", HttpStatus.BAD_REQUEST),
    PAYMENT_ERROR(1016, "Payment error", HttpStatus.BAD_REQUEST),
    INVALID_DATA_WITH_USERID(1015,"Please try again, something wrong with this userId",HttpStatus.BAD_REQUEST)

    ;

    private int code;
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    private HttpStatusCode statusCode;
}
