package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.response.FishResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/fish")
public class FishController {

    @PostMapping("/add_Fish")
    public ApiResponse<FishResponse> addFish(){

        ApiResponse<FishResponse> apiResponse = new ApiResponse<>();

        return apiResponse;
    }
}
