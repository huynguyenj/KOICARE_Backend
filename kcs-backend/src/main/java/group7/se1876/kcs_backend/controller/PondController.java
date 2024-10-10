package group7.se1876.kcs_backend.controller;


import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.repository.UserRepository;
import group7.se1876.kcs_backend.service.PondService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/pond")

public class PondController {

    private PondService pondService;

    //Add pond
    @PostMapping("/add_Pond")
    public ApiResponse<PondResponse> createNewPond(@RequestBody AddPondRequest request){

        ApiResponse<PondResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(pondService.addPond(request));

        return apiResponse;
    }

    //Get all pond
    @GetMapping("/getAllPond")
    public ApiResponse<List<PondResponse>> getAllPonds(){

        ApiResponse<List<PondResponse>> listPonds = new ApiResponse<>();
        listPonds.setResult(pondService.getAllPonds());

        return listPonds;
    }

    @GetMapping("/getPondInfo/{pondid}")
    public ApiResponse<PondResponse> getPond(@PathVariable("pondid") Long pondId){

        ApiResponse<PondResponse> pondInfo = new ApiResponse<>();
        pondInfo.setResult(pondService.getPondInfo(pondId));

        return pondInfo;
    }

    //Update pond
    @PutMapping("/update_Pond/{pondid}")
    public ApiResponse<PondResponse> updatePondInfo(@PathVariable("pondid") Long pondId, @RequestBody PondUpdateRequest pondUpdateRequest){

        ApiResponse<PondResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(pondService.updatePond(pondId,pondUpdateRequest));

        return apiResponse;

    }

    //Delete pond
    @DeleteMapping("/delete_Pond/{pondid}")
    public ApiResponse<String> deletePond(@PathVariable("pondid") Long pondId){

        ApiResponse<String> apiResponse = new ApiResponse<>();

        pondService.deletePond(pondId);
        apiResponse.setResult("Delete successfully");

        return apiResponse;
    }

    @PutMapping("/addFishToPond/pond/{pondid}/fish/{fishid}")
    public ApiResponse<PondResponse> updatePondInfo(@PathVariable("pondid") Long pondId, @PathVariable("fishid") Long fishid){

        ApiResponse<PondResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(pondService.addFishToPond(pondId,fishid));

        return apiResponse;

    }

}
