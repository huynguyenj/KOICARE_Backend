package group7.se1876.kcs_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // No using 1010 it already used
    INVALID_KEY(1000, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNCATAGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed(please check your username or your email)", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1002, "User password must at least 8 character.", HttpStatus.BAD_REQUEST),
    INVALID_USERID(1003, "This userID is not existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User invalid(please check your username or your password)", HttpStatus.NOT_FOUND),
    UNAUTHENDICATED(1005, "Unauthendicated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission for this function", HttpStatus.FORBIDDEN),
    ORDER_DETAIL_NOT_FOUND(1007, "Order detail not found", HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND(1008, "Item not found", HttpStatus.NOT_FOUND),
    ITEM_EXISTED(1009, "Item existed", HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK(1010, "Out of stock", HttpStatus.BAD_REQUEST),

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
