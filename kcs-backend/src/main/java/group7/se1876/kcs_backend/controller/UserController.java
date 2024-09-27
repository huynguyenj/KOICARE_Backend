package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private UserService userService;

    //Register
    @PostMapping("/user")
    public ApiResponse<UserResponse> register(@RequestBody @Valid UserDto userDto){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.register(userDto));

      return apiResponse;
    }

    //Get users
    @GetMapping("/getUsers")
    public ApiResponse<List<UserResponse>> getAllUser(){

        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getAllUser());

        return apiResponse;

    }
    //Get user info
    @GetMapping("{id}")
    public  ApiResponse<UserResponse> getUser(@PathVariable("id") Long userId ){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getUser(userId));

        return apiResponse;
    }
    // Get my info
    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo(){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getMyInfo());

        return apiResponse;

    }

    //Update user
    @PutMapping("/update_User/{userid}")
    public ApiResponse<UserResponse> updateUser (@PathVariable Long userId, @RequestBody UserUpdateRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.updateUser(userId, request));

        return apiResponse;

    }

}
