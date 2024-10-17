package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.CreateShopRequest;
import group7.se1876.kcs_backend.dto.request.UpdateShopRequest;
import group7.se1876.kcs_backend.dto.response.ShopResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.service.ShopService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/shop")
@CrossOrigin
public class ShopController {
    private ShopService shopService;

    @PostMapping("/createShop")
    public ApiResponse<ShopResponse> createShop(@RequestBody CreateShopRequest request){

        ApiResponse<ShopResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(shopService.createShop(request));

        return apiResponse;
    }

    @PutMapping("/updateShop/{shopId}")
    public ApiResponse<ShopResponse> updateShop(@PathVariable("shopId")Long shopId,@RequestBody UpdateShopRequest request){

        ApiResponse<ShopResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(shopService.updateShop(shopId,request));

        return apiResponse;
    }

    @DeleteMapping("/deleteShop/{shopId}")
    public ApiResponse<String> deleteShop(@PathVariable("shopId")Long shopId){

        ApiResponse<String> apiResponse = new ApiResponse<>();
        shopService.deleteShop(shopId);
        apiResponse.setResult("Delete successfully");

        return apiResponse;
    }

    //Get shop info
    @GetMapping("/getShop")
    public ApiResponse<ShopResponse> getShop(){

        ApiResponse<ShopResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(shopService.getShop());

        return apiResponse;
    }

    //Get all shop
    @GetMapping("/getAllShop")
    public ApiResponse<List<ShopResponse>> getAllShop(){

        ApiResponse<List<ShopResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(shopService.getALlShop());

        return apiResponse;
    }
}
