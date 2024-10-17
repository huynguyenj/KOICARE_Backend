//package group7.se1876.kcs_backend.service;
//
//import group7.se1876.kcs_backend.dto.request.PaymentRequest;
//import group7.se1876.kcs_backend.dto.response.TransactionHistoryResponse;
//import group7.se1876.kcs_backend.exception.AppException;
//import group7.se1876.kcs_backend.exception.ErrorCode;
//import group7.se1876.kcs_backend.repository.TransactionRepository;
//import group7.se1876.kcs_backend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
//public class PaymentService {
//    private final TransactionRepository transactionRepository;
//    private final UserRepository userRepository;
//
//    private static final Logger log = (Logger) LoggerFactory.getLogger(PaymentService.class);
//    String vnp_TmnCode = "3N379WTD";
//    String vnp_HashSecret = "AHONNZ0PW0K3XM598PUQ8BO0VODB6PXV";
//    String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//    // Sử dụng vnp_ReturnUrl từ PaymentService1
//    public static String vnp_ReturnUrl = "http://localhost:8080/vnpay_jsp/vnpay_return.jsp";
//    private final java.util.Map<String, String> paymentTokens = new java.util.HashMap<>();
//
//    public List<TransactionHistoryResponse> getTransactionHistory() {
//        return transactionRepository.findAll().stream()
//                .map(transaction -> new TransactionHistoryResponse(
//                        transaction.getId(),
//                        transaction.getUsername(),
//                        transaction.getDetails(),
//                        transaction.getDate().toString(),
//                        transaction.getAmount()
//                ))
//                .collect(Collectors.toList());
//    }
//
//    public String createPayment(PaymentRequest request) {
//        try {
//            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            Map<String, String> vnp_Params = new HashMap<>();
//            vnp_Params.put("vnp_Version", "2.1.0");
//            vnp_Params.put("vnp_Command", "pay");
//            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//            vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
//            vnp_Params.put("vnp_CurrCode", "VND");
//            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
//            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_Params.get("vnp_TxnRef"));
//            vnp_Params.put("vnp_OrderType", "other");
//            vnp_Params.put("vnp_Locale", "vn");
//            vnp_Params.put("vnp_IpAddr", request.getIpAddr());
//
//            Calendar cld = Calendar.getInstance(java.util.TimeZone.getTimeZone("Etc/GMT+7"));
//            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
//            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//            cld.add(Calendar.MINUTE, 15);
//            String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
//            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder hashData = new StringBuilder();
//            StringBuilder query = new StringBuilder();
//            Iterator<String> itr = fieldNames.iterator();
//            while (itr.hasNext()) {
//                String fieldName = itr.next();
//                String fieldValue = vnp_Params.get(fieldName);
//                if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                    hashData.append(fieldName);
//                    hashData.append('=');
//                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                    query.append('=');
//                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    if (itr.hasNext()) {
//                        query.append('&');
//                        hashData.append('&');
//                    }
//                }
//            }
//            String queryUrl = query.toString();
//            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
//            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//
//            //Lưu token của người dùng với transaction reference
//            paymentTokens.put(vnp_Params.get("vnp_TxnRef"), vnp_SecureHash);
//
//            return vnp_Url + "?" + queryUrl;
//        } catch (Exception e) {
//            throw new AppException(ErrorCode.PAYMENT_ERROR);
//        }
//    }
//
//    private String hmacSHA512(String key, String data) throws Exception {
//        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//        sha512_HMAC.init(secret_key);
//        return bytesToHex(sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
//    }
//    private static String bytesToHex(byte[] bytes) {
//        final char[] hexArray = "0123456789ABCDEF".toCharArray();
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//}
