package group7.se1876.kcs_backend.service;

import com.google.cloud.storage.BlobInfo;
import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.entity.Fish;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.PondMapper;
import group7.se1876.kcs_backend.repository.FishRepository;
import group7.se1876.kcs_backend.repository.PondRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class PondService {
    private PondRepository pondRepository;
    private UserRepository userRepository;
    private FishRepository fishRepository;
    private PondMapper pondMapper;
    private FirebaseStorageService firebaseStorageService;

    public PondResponse addPond(AddPondRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));


        Pond pond = pondMapper.mapToPond(request);

        // Upload image to Firebase
        if (request.getPondImg() != null && !request.getPondImg().isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(request.getPondImg(),"pond-images/");  // Corrected
                pond.setPondImg(imageUrl);  // Assuming Pond entity has pondImg field
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }


        pond.setUser(user);
        pondRepository.save(pond);

        user.getPonds().add(pond);
        userRepository.save(user);

        return pondMapper.mapToPondResponse(pond);

    }

    //Get all ponds
    public List<PondResponse> getAllPonds(){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_USERID));

        Set<Pond> ponds = user.getPonds();

        return ponds.stream().map((pond)-> pondMapper.mapToPondResponse(pond)).collect(Collectors.toList());
    }

    //Get pond info
    public PondResponse getPondInfo(Long pondId){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        if(userId != pond.getUser().getUserId())
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);

        return pondMapper.mapToPondResponse(pond);
    }

    //Delete pond
    public void deletePond(Long pondId){

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        User user= pond.getUser();

        try {
            if (user!=null){
//                firebaseStorageService.deleteFile(pond.getPondImg());
                user.getPonds().remove(pond);
                userRepository.save(user);
            }
        }catch (Exception e){
            throw new AppException(ErrorCode.DELETE_FAIL);
        }

        pondRepository.delete(pond);

    }

    //Update pond
    public PondResponse updatePond(Long pondId, PondUpdateRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (userId != pond.getUser().getUserId())
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);


        pond.setPondName(request.getPondName());

        // Upload image to Firebase
        if (request.getPondImg() != null && !request.getPondImg().isEmpty()) {
            try {
//                firebaseStorageService.deleteFile(pond.getPondImg());
                String imageUrl = firebaseStorageService.uploadFile(request.getPondImg(),"pond-images/");  // Corrected
                pond.setPondImg(imageUrl);  // Assuming Pond entity has pondImg field
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }

        pond.setDate(new Date());
        pond.setDepth(request.getDepth());
        pond.setDrainCount(request.getDrainCount());
        pond.setPumpCapacity(request.getPumpCapacity());
        pond.setSize(request.getSize());
        pond.setVolume(request.getVolume());

        pondRepository.save(pond);

        return pondMapper.mapToPondResponse(pond);

    }

    //Add fish to pond
    public PondResponse addFishToPond(Long pondId,Long fishId){

        Fish fish = fishRepository.findById(fishId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if(fish.getOwner().getUserId() != userId || pond.getUser().getUserId() != userId){
            throw  new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        if (!fish.getPonds().isEmpty()){
            for (Pond oldPond: new ArrayList<>(fish.getPonds())){
                oldPond.getFish().remove(fish);
                pondRepository.save(oldPond);
            }
        }


        double volume = pond.getVolume()*1000;
        double maxKoi = volume/1000;

        int amountKoi = 0;
        for (Fish f: pond.getFish()){
            amountKoi++;
        }
        System.out.println(amountKoi);

        if (amountKoi > maxKoi){
            throw new AppException(ErrorCode.LIMITED_AMOUNT);
        }

        pond.getFish().add(fish);
        pondRepository.save(pond);

        fish.getPonds().add(pond);
        fishRepository.save(fish);

        return pondMapper.mapToPondResponse(pond);
    }

}
