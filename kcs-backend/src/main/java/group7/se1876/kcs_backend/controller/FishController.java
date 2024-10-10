package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.AddFishRequest;
import group7.se1876.kcs_backend.dto.request.FishUpdateRequest;
import group7.se1876.kcs_backend.dto.response.FishResponse;
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
    public ApiResponse<FishResponse> addFish(@RequestBody AddFishRequest request){

        ApiResponse<FishResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(fishService.addFish(request));

        return apiResponse;
    }

    //Update fish
    @PutMapping("/update_Fish/{fishid}")
    public ApiResponse<FishResponse> updateFish(@PathVariable("fishid") Long fishId ,@RequestBody FishUpdateRequest request){

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

}
