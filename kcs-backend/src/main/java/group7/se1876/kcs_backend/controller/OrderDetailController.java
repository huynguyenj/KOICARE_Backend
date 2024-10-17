package group7.se1876.kcs_backend.controller;


import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/active")
    public ResponseEntity<List<OrderDetail>> getAllActiveOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllActiveOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable int id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("OrderDetail deleted successfully");
    }
}
