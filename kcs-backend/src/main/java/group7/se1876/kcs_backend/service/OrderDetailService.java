package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getAllActiveOrderDetails() {
        return orderDetailRepository.findByIsDeletedFalse();
    }
    public void deleteOrderDetail(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
        if (orderDetail != null) {
            orderDetail.setDeleted(true);
            orderDetailRepository.save(orderDetail);
        }
    }
}
