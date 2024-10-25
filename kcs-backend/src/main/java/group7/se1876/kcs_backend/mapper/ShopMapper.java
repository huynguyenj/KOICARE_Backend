package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.CreateShopRequest;
import group7.se1876.kcs_backend.dto.response.OrderDetailResponse;
import group7.se1876.kcs_backend.dto.response.ShopResponse;
import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

    public static Shop mapToShop(CreateShopRequest request){
        return new Shop(
                request.getShopId(),
                request.getShopName(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                request.getContactInfo(),
                request.isStatus(),
                null,
                null,
                null
        );
    }

    public static ShopResponse mapToShopResponse(Shop shop){
        return new ShopResponse(
                shop.getShopId(),
                shop.getShopName(),
                shop.getAddress(),
                shop.getPhone(),
                shop.getEmail(),
                shop.getContactInfo(),
                shop.isStatus(),
                shop.getOwnerShop().getUserName()
        );
    }
    public static OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail){
        return new OrderDetailResponse(
                orderDetail.getId(),
                orderDetail.getQuantity(),
                orderDetail.getPrice(),
                orderDetail.getUserName(),
                orderDetail.getAddress(),
                orderDetail.getPhone(),
                orderDetail.getDate()
        );
    }
}
