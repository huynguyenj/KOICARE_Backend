package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddBlogRequest;
import group7.se1876.kcs_backend.dto.request.BlogUpdateRequest;
import group7.se1876.kcs_backend.dto.response.BlogResponse;
import group7.se1876.kcs_backend.entity.Blog;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.UserMapper;
import group7.se1876.kcs_backend.repository.BlogRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {

    private UserRepository userRepository;
    private BlogRepository blogRepository;
    private UserMapper userMapper;
    private FirebaseStorageService firebaseStorageService;
    //Add blog
    public BlogResponse addBlog(AddBlogRequest request){


        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        Blog blog = userMapper.mapToBlog(request);

        // Upload image to Firebase
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(request.getImage(),"blog-images/");  // Corrected
                blog.setImage(imageUrl); // Assuming Pond entity has pondImg field
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }
        blog.setUser(user);
        user.getBlogs().add(blog);

        blogRepository.save(blog);
        userRepository.save(user);



        return userMapper.mapToBlogResponse(blog);

    }

    //Get my blogs
    public List<BlogResponse> getMyBlog(){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        List<Blog> blogList = blogRepository.findByUser_UserId(userId);

        return blogList.stream().map((blog)->userMapper.mapToBlogResponse(blog)).collect(Collectors.toList());
    }

    //Get all blogs
    public List<BlogResponse> getAllBlog(){

        List<Blog> blogList = blogRepository.findAll();

        return blogList.stream().map((blog)->userMapper.mapToBlogResponse(blog)).collect(Collectors.toList());
    }

    //Get all blogs
    public BlogResponse getBlog(Long blogId){

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        return userMapper.mapToBlogResponse(blog);
    }

    //Update my blog
    public BlogResponse updateMyBlog(Long blogId, BlogUpdateRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (blog.getUser().getUserId()!= userId){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }
        // Upload image to Firebase
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                firebaseStorageService.deleteFile(blog.getImage());
                String imageUrl = firebaseStorageService.uploadFile(request.getImage(),"blog-images/");
                blog.setImage(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }

        blog.setContent(request.getContent());
        blog.setTitle(request.getTitle()) ;
        blog.setPublishedDate(request.getPublishedDate());

        blogRepository.save(blog);

        return userMapper.mapToBlogResponse(blog);
    }

    //Delete blog
    public void deleteBlog(Long blogId){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (blog.getUser().getUserId()!= userId){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        user.getBlogs().remove(blog);
        userRepository.save(user);

        blogRepository.deleteById(blogId);

    }

}
