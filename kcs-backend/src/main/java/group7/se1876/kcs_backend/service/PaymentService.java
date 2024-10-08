package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.repository.PaymentRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private final UserRepository userRepository;

    private static final Logger log = (Logger) LoggerFactory.getLogger(PaymentService.class);
    String vnp_TmnCode = "ZYT12LWX";

}
