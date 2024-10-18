package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.CreateShopRequest;
import group7.se1876.kcs_backend.dto.request.UpdateShopRequest;
import group7.se1876.kcs_backend.dto.response.ShopResponse;
import group7.se1876.kcs_backend.entity.Shop;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.ShopMapper;
import group7.se1876.kcs_backend.repository.ShopRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private UserRepository userRepository;

    //Create shop
    public ShopResponse createShop(CreateShopRequest shopRequest) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USERID));

        Shop shop = shopMapper.mapToShop(shopRequest);

        shop.setOwnerShop(user);
        shopRepository.save(shop);

        user.setShop(shop);
        userRepository.save(user);

        System.out.println("hello");
        return shopMapper.mapToShopResponse(shop) ;
    }

    // Update shop
    public ShopResponse updateShop(Long shopId, UpdateShopRequest shopRequest) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        // Check if the shop belongs to the user
        if (!shop.getOwnerShop().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        shop.setShopName(shopRequest.getShopName());
        shop.setAddress(shopRequest.getAddress());
        shop.setPhone(shopRequest.getPhone());
        shop.setEmail(shopRequest.getEmail());
        shop.setContactInfo(shopRequest.getContactInfo());
        shop.setStatus(shopRequest.isStatus());
        shopRepository.save(shop);

        return shopMapper.mapToShopResponse(shop) ;
    }

    // Delete shop
    public void deleteShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!shop.getOwnerShop().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        User owner = shop.getOwnerShop();
        owner.setShop(null);
        userRepository.save(owner);

        System.out.println("Deleting shop with ID: " + shopId);
        shopRepository.deleteById(shopId);
        System.out.println("Shop deleted successfully.");

    }

    // Get shop for specific user
    public ShopResponse getShop() {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        return shopMapper.mapToShopResponse(shop) ;
    }

    // Get all shop
    public List<ShopResponse> getALlShop() {

        List<Shop> shops = shopRepository.findAll();

        return shops.stream().map((shop)->shopMapper.mapToShopResponse(shop)).collect(Collectors.toList());
    }
}

