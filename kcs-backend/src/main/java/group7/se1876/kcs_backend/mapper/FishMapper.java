package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.AddFishRequest;
import group7.se1876.kcs_backend.dto.response.FishResponse;
import group7.se1876.kcs_backend.entity.Fish;
import org.springframework.stereotype.Component;

@Component
public class FishMapper {
    public static Fish mapToFish(AddFishRequest request){
        return new Fish(
                request.getFishId(),
                request.getFishName(),
                request.getFishImg(),
                request.getFishSize(),
                request.getFishShape(),
                request.getFishAge(),
                request.getFishWeight(),
                request.getFishGender(),
                request.getFishHealth(),
                request.getFishType(),
                request.getOrigin(),
                request.getPrice(),
                null,
                null
        );
    }
    public static FishResponse mapToFishResponse(Fish fish){
        return new FishResponse(
                fish.getFishId(),
                fish.getFishName(),
                fish.getFishImg(),
                fish.getFishSize(),
                fish.getFishShape(),
                fish.getFishAge(),
                fish.getFishWeight(),
                fish.getFishGender(),
                fish.getFishHealth(),
                fish.getFishType(),
                fish.getOrigin(),
                fish.getPrice()
        );

    }

}
