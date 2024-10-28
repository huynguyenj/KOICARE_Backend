package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.PaymentRequest;
import group7.se1876.kcs_backend.dto.response.TransactionHistoryResponse;
import group7.se1876.kcs_backend.entity.Transaction;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.repository.TransactionRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    String vnp_TmnCode = "C0DZPJH8";
    String vnp_HashSecret = "ZPQF7HG5PUJZVPPUG09WT6VFQ7X9GQAQ";
    String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    // Sử dụng vnp_ReturnUrl từ PaymentService1
    String vnp_ReturnUrl = "http://localhost:5173/userhome/paymentSuccess";
    private final Map<String, String> paymentTokens = new ConcurrentHashMap<>();

    public List<TransactionHistoryResponse> getTransactionHistory() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionHistoryResponse(
                        transaction.getId(),
                        transaction.getUsername(),
                        transaction.getDetails(),
                        transaction.getDate(),
                        transaction.getAmount()
                ))
                .collect(Collectors.toList());
    }

    public String createPayment(PaymentRequest request) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_Params.get("vnp_TxnRef"));
            vnp_Params.put("vnp_OrderType", "order");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", request.getIpAddr());

            Calendar cld = Calendar.getInstance(java.util.TimeZone.getTimeZone("Etc/GMT+7"));
            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            User user= userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            //Lưu token của người dùng với transaction reference
            paymentTokens.put(vnp_Params.get("vnp_TxnRef"), user.getUserName());

            for (Map.Entry<String, String> entry : paymentTokens.entrySet()) {
                System.out.println(entry.getValue());
                log.info("TxnRef: {}, UserName: {}", entry.getKey(), entry.getValue());
            }
            return vnp_Url + "?" + query.toString();

        } catch (Exception e) {
            log.error("Error occurred during payment creation: " + e.getMessage(), e);
            throw new AppException(ErrorCode.PAYMENT_ERROR);
        }
    }

    public boolean verifyPayment( Map<String, String> params) throws UnsupportedEncodingException {
        String vnp_SecureHash = params.get("vnp_SecureHash").toUpperCase();
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        for (Map.Entry<String, String> entry : paymentTokens.entrySet()) {
            System.out.println(entry.getValue());
            log.info("TxnRef: {}, UserName: {}", entry.getKey(), entry.getValue());
        }
        String vnp_TxnRef = params.get("vnp_TxnRef");


        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                hashData.append('&');
            }
        }
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1); // Xóa ký tự '&' cuối
        }

        try {
            String hashString = hmacSHA512(vnp_HashSecret, hashData.toString());
            System.out.println("Hash Data: " + hashData);
            System.out.println("Generated Hash: " + hashString);
            System.out.println("Received Secure Hash: " + vnp_SecureHash);
            if (!hashString.equals(vnp_SecureHash)) {
                log.error("Secure hash mismatch. Expected: {}, Actual: {}", hashString, vnp_SecureHash);
                return false;
            }
        } catch (Exception e) {
            log.error("Error while verifying payment hash", e);
            return false;
        }

        String responseCode = params.get("vnp_ResponseCode");
        System.out.println("Response Code: " + responseCode);
        if ("00".equals(responseCode)) {
            System.out.println("Payment success!");
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            long amount = Long.parseLong(params.get("vnp_Amount")) / 100;
            System.out.println("Parsed Amount: " + amount);

            // Lưu giao dịch vào cơ sở dữ liệu
            Transaction transaction = new Transaction();
            transaction.setUsername(user.getUserName());
            transaction.setDetails(params.get("vnp_OrderInfo"));
            transaction.setDate(new Date());
            transaction.setBankCode(params.get("vnp_BankCode"));
            transaction.setAmount((double) amount);
            transactionRepository.save(transaction);

            return true;
        } else {
            log.info("Response code is not 00. Actual response: {}", responseCode);
        }
        return false;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKeySpec);
        byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
