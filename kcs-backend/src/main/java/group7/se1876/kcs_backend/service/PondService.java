package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.PondMapper;
import group7.se1876.kcs_backend.repository.PondRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PondService {
        private PondRepository pondRepository;
        private UserRepository userRepository;
        private PondMapper pondMapper;

        //Add pond
        public PondResponse addPond(AddPondRequest request){

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));

            Pond pond = pondMapper.mapToPond(request);
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
        //Delete pond
    public void deletePond(Long pondId){

            Pond pond = pondRepository.findById(pondId)
                    .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

            User user= pond.getUser();

            try {
                if (user!=null){
                    user.getPonds().remove(pond);
                    userRepository.save(user);
                }
            }catch (Exception e){
                throw new AppException(ErrorCode.DELETE_FAIL);
            }

            pondRepository.delete(pond);

    }

    public PondResponse updatePond(Long pondId, PondUpdateRequest request){

            Pond pond = pondRepository.findById(pondId)
                    .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

            pond.setPondName(request.getPondName());
            pond.setPondImg(request.getPondImg());
            pond.setDate(new Date());
            pond.setDepth(request.getDepth());
            pond.setDrainCount(request.getDrainCount());
            pond.setPumpCapacity(request.getPumpCapacity());
            pond.setSaltAmount(request.getSaltAmount());
            pond.setSize(request.getSize());
            pond.setVolume(request.getVolume());

            pondRepository.save(pond);

            return pondMapper.mapToPondResponse(pond);

    }
}
