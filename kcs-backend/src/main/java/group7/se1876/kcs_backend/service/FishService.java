package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddFishRequest;
import group7.se1876.kcs_backend.dto.request.FishUpdateRequest;
import group7.se1876.kcs_backend.dto.response.FishResponse;
import group7.se1876.kcs_backend.entity.Fish;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.FishMapper;
import group7.se1876.kcs_backend.repository.FishRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FishService {

    private FishRepository fishRepository;
    private FishMapper fishMapper;
    private UserRepository userRepository;

    //Add fish
    public FishResponse addFish(AddFishRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_USERID));

        Fish fish = fishMapper.mapToFish(request);
        fish.setOwner(user);
        fishRepository.save(fish);

        user.getFishOwned().add(fish);
        userRepository.save(user);

        return fishMapper.mapToFishResponse(fish) ;
    }

    //Get all fish
    public List<FishResponse> getAllFish(){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_USERID));

        Set<Fish> ownerFish = user.getFishOwned();

        return ownerFish.stream().map((fish)-> fishMapper.mapToFishResponse(fish)).collect(Collectors.toList());
    }

    public FishResponse getFishInfo(Long fishId){

        Fish fish = fishRepository.findById(fishId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        return fishMapper.mapToFishResponse(fish);
    }

    //Update fish
    public FishResponse updateFish(Long fishId, FishUpdateRequest request){

        Fish fish = fishRepository.findById(fishId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        fish.setFishName(request.getFishName());
        fish.setFishImg(request.getFishImg());
        fish.setFishSize(request.getFishSize());
        fish.setFishShape(request.getFishShape());
        fish.setFishAge(request.getFishAge());
        fish.setFishWeight(request.getFishWeight());
        fish.setFishGender(request.getFishGender());
        fish.setFishGender(request.getFishGender());
        fish.setFishHealth(request.getFishHealth());
        fish.setFishType(request.getFishType());
        fish.setOrigin(request.getOrigin());
        fish.setPrice(request.getPrice());

        fishRepository.save(fish);

        return fishMapper.mapToFishResponse(fish);
    }

    //Delete fish
    public void deleteFish(Long fishId){

        Fish fish = fishRepository.findById(fishId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        User user = fish.getOwner();

        try {
            if (user!=null){
                user.getFishOwned().remove(fish);
                userRepository.save(user);
            }
        }catch (Exception e){
            throw new AppException(ErrorCode.DELETE_FAIL);
        }
        fishRepository.delete(fish);

    }
}
