package group7.se1876.kcs_backend.controller;


import group7.se1876.kcs_backend.dto.request.PaymentRequest;
import group7.se1876.kcs_backend.dto.response.TransactionHistoryResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.service.PaymentService;
import group7.se1876.kcs_backend.service.ProductService;
import group7.se1876.kcs_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    UserService userService;
    ProductService productService;

    @PostMapping("/create")
    public ApiResponse<String> createPayment(@RequestBody PaymentRequest request) {
        String paymentUrl = paymentService.createPayment(request);
        return ApiResponse.<String>builder()
                .result(paymentUrl)
                .build();
    }

    @GetMapping("/history")
    public ApiResponse<List<TransactionHistoryResponse>> getTransactionHistory() {
        List<TransactionHistoryResponse> history = paymentService.getTransactionHistory();
        return ApiResponse.<List<TransactionHistoryResponse>>builder()
                .result(history)
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<String> verifyPayment(@RequestParam Map<String, String> params) {
        try {
            boolean isSuccess = paymentService.verifyPayment(params);
            if (isSuccess) {
                return ApiResponse.<String>builder()
                        .code(0)
                        .result("Payment success!")
                        .build();
            } else {
                return ApiResponse.<String>builder()
                        .code(1)
                        .result("Payment verification failed!")
                        .build();
            }
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .code(-1)
                    .result("Payment verification failed!")
                    .build();
        }
    }
}
