package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.AddFishDevelopmentHistoryRequest;
import group7.se1876.kcs_backend.dto.request.AddFishRequest;
import group7.se1876.kcs_backend.dto.request.FishUpdateRequest;
import group7.se1876.kcs_backend.dto.response.FishResponse;
import group7.se1876.kcs_backend.dto.response.FoodCalculationResponse;
import group7.se1876.kcs_backend.dto.response.KoiFishDevelopmentResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.service.FishService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/fish")
public class FishController {

    private FishService fishService;

    //Add fish
    @PostMapping("/add_Fish")
    public ApiResponse<FishResponse> addFish(@ModelAttribute AddFishRequest request){

        ApiResponse<FishResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.addFish(request));

        return apiResponse;
    }

    //Update fish
    @PutMapping("/update_Fish/{fishid}")
    public ApiResponse<FishResponse> updateFish(@PathVariable("fishid") Long fishId ,@ModelAttribute FishUpdateRequest request){

        ApiResponse<FishResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.updateFish(fishId,request));

        return apiResponse;
    }

    //Delete fish
    @DeleteMapping("/delete_Fish/{fishid}")
    public ApiResponse<String > deleteFish(@PathVariable("fishid") Long fishId){

        ApiResponse<String> apiResponse = new ApiResponse<>();
        fishService.deleteFish(fishId);
        apiResponse.setResult("Delete successfully");

        return apiResponse;
    }

    //Get all fish
    @GetMapping("/getAllFish")
    public ApiResponse<List<FishResponse>> getAllFish(){

        ApiResponse<List<FishResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.getAllFish());

        return apiResponse;
    }

    //Get fish info
        @GetMapping("/getFish/{fishid}")
    public ApiResponse<FishResponse> getFish(@PathVariable("fishid") Long fishId){

        ApiResponse <FishResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.getFishInfo(fishId));

        return apiResponse;
    }

    //Add fish development history
    @PostMapping("/{fishId}/addFishDevelopment")
    public ApiResponse<KoiFishDevelopmentResponse> addDevelopmentHistory(@PathVariable("fishId")Long fishId,
                                                                         @RequestBody AddFishDevelopmentHistoryRequest request){

        ApiResponse<KoiFishDevelopmentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.addFishHistory(fishId,request));

        return apiResponse;
    }

    //Get fish development history
    @GetMapping("/{fishid}/getFishDevelopment")
    public ApiResponse<List<KoiFishDevelopmentResponse>> getFishHistories(@PathVariable("fishid")Long fishId){

        ApiResponse<List<KoiFishDevelopmentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.getFishHistories(fishId));

        return apiResponse;

    }

    //Create Food calculation
    @PostMapping("{fishId}/foodCalculation")
    public ApiResponse<FoodCalculationResponse> addFoodCalcultion(@PathVariable("fishId")Long fishId){

        ApiResponse<FoodCalculationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.addFoodCalculation(fishId));

        return apiResponse;
    }

    //Get all food calculation of fish
    @GetMapping("{fishId}/getFoodCalculations")
    public ApiResponse<List<FoodCalculationResponse>> getAllFoodCalculations(@PathVariable("fishId") Long fishId){

        ApiResponse<List<FoodCalculationResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.getAllFoodCalculation(fishId));

        return apiResponse;
    }


}
