package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.*;
import group7.se1876.kcs_backend.dto.response.BlogResponse;
import group7.se1876.kcs_backend.dto.response.TrackingUserResponse;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.exception.ApiResponse;
import group7.se1876.kcs_backend.service.BlogService;
import group7.se1876.kcs_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private UserService userService;
    private BlogService blogService;

    //Register
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody @Valid UserDto userDto,@RequestParam String userRoleChoice){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.register(userDto,userRoleChoice));

        return apiResponse;
    }

    //Get users (admin)
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
    public ApiResponse<UserResponse> updateUser (@PathVariable("userid") Long userId, @RequestBody UserUpdateRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));

        return apiResponse;

    }

    //Delete user (admin)
    @DeleteMapping("/delete/{userid}")
    public ApiResponse<String> deleteUser(@PathVariable("userid") Long userId){

        userService.deleteUser(userId);

        ApiResponse<String> result = new ApiResponse<>();
        result.setMessage("Delete succesfully");

        return result;
    }

    //Set status user (admin)
    @PutMapping("/setStatus/{userId}")
    public ApiResponse<UserResponse> setActiveAccount(@PathVariable("userId") Long userId, @RequestParam String decision){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.setStatusAccount(userId,decision));

        return apiResponse;
    }

    //Set role user (admin)
    @PutMapping("/setRole/{userId}")
    public ApiResponse<UserResponse> setRole(@PathVariable("userId") Long userId, @RequestParam String role){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.setRole(userId,role));

        return apiResponse;
    }

    //Post blog
    @PostMapping("/user/postBlog")
    public ApiResponse<BlogResponse> addBlog(@ModelAttribute AddBlogRequest request){


        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(blogService.addBlog(request));

        return apiResponse;
    }

    //Get my blogs
    @GetMapping("/user/getMyBlogs")
    public ApiResponse<List<BlogResponse>> getMyBlogs(){

        ApiResponse<List<BlogResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(blogService.getMyBlog());

        System.out.println("hello");
        return apiResponse;
    }

    //Get all blogs
    @GetMapping("/user/getAllBlogs")
    public ApiResponse<List<BlogResponse>> getAllBlogs(){

        ApiResponse<List<BlogResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(blogService.getAllBlog());

        return apiResponse;
    }
    //comment something
    //get blog by id
    @GetMapping("/user/getBlog/{blogId}")
    public ApiResponse<BlogResponse> getBlog(@PathVariable("blogId")Long blogId){

        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(blogService.getBlog(blogId));

        return apiResponse;
    }

    //Update my blog
    @PutMapping("/user/updateMyBlog/{blogId}")
    public ApiResponse<BlogResponse> updateMyBlog(@PathVariable("blogId") Long blogId,@ModelAttribute BlogUpdateRequest request){

        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(blogService.updateMyBlog(blogId,request));

        return apiResponse;
    }

    //Delete my blog
    @DeleteMapping("/user/deleteMyBlog/{blogId}")
    public ApiResponse<String> deleteMyBlog(@PathVariable("blogId") Long blogId){

        ApiResponse<String> apiResponse = new ApiResponse<>();
        blogService.deleteBlog(blogId);
        apiResponse.setResult("Delete successfully");

        return apiResponse;
    }



}
