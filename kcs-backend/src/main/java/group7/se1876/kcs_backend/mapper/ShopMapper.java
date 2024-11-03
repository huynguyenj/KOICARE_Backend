package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.CreateShopRequest;
import group7.se1876.kcs_backend.dto.request.RatingRequest;
import group7.se1876.kcs_backend.dto.response.OrderDetailResponse;
import group7.se1876.kcs_backend.dto.response.RatingResponse;
import group7.se1876.kcs_backend.dto.response.ShopResponse;
import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.entity.RatingProduct;
import group7.se1876.kcs_backend.entity.Shop;
import group7.se1876.kcs_backend.entity.User;
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
                orderDetail.getDate(),
                orderDetail.getProductName()
        );
    }

    public static RatingProduct mapToRatingProduct(RatingRequest request){
        return new RatingProduct(
                request.getRatingId(),
                request.getContent(),
                null,
                null,
                request.getStar(),
                null,
                null,
                null
        );
    }

    public static RatingResponse mapToRatingProductResponse(RatingProduct request){

        return new RatingResponse(
                request.getRatingId(),
                request.getContent(),
                request.getImage(),
                request.getDate(),
                request.getStar(),
                request.getProduct().getProductName(),
                request.getUserName()

        );
    }
}
