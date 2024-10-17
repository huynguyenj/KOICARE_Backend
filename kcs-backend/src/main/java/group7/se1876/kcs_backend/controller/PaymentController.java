//package group7.se1876.kcs_backend.controller;
//
//
//import group7.se1876.kcs_backend.dto.request.PaymentRequest;
//import group7.se1876.kcs_backend.exception.ApiResponse;
//import group7.se1876.kcs_backend.service.PaymentService;
//import group7.se1876.kcs_backend.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import static lombok.AccessLevel.PRIVATE;
//
//@RestController
//@RequestMapping("/payment")
//@RequiredArgsConstructor
//@FieldDefaults(level = PRIVATE, makeFinal = true)
//public class PaymentController {
//    PaymentService paymentService;
//    UserService userService;
//
//    @PostMapping("/create")
//    public ApiResponse<String> createPayment(@RequestBody PaymentRequest request) {
//        String paymentUrl = paymentService.createPayment(request);
//        return ApiResponse.<String>builder()
//                .result(paymentUrl)
//                .build();
//    }
//}
