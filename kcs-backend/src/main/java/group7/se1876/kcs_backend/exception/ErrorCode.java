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
    DELETE_FAIL(1009,"Delete fail",HttpStatus.BAD_REQUEST)
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
