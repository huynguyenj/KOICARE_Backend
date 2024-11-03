package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.AddFishDevelopmentHistoryRequest;
import group7.se1876.kcs_backend.dto.request.AddFishRequest;
import group7.se1876.kcs_backend.dto.response.*;
import group7.se1876.kcs_backend.entity.Fish;
import group7.se1876.kcs_backend.entity.FishDevelopmentHistory;
//import group7.se1876.kcs_backend.entity.FoodCalculation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FishMapper {
    public static Fish mapToFish(AddFishRequest request) {
        return new Fish(
                request.getFishId(),
                request.getFishName(),
                null,
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
                null,
                null

        );
    }

    public static FishResponse mapToFishResponse(Fish fish) {

        List<KoiFishDevelopmentResponse> fishHistories = (fish.getFishDevelopmentHistories()!=null)
                ?fish.getFishDevelopmentHistories().stream()
                .map(fishDevelopmentHistory-> new KoiFishDevelopmentResponse(
                        fishDevelopmentHistory.getHistoryId(),
                        fishDevelopmentHistory.getSize(),
                        fishDevelopmentHistory.getDate(),
                        fishDevelopmentHistory.getAge(),
                        fishDevelopmentHistory.getWeight(),
                        fish.getFishName())).collect(Collectors.toList()): new ArrayList<>();

        List<PondWithFishResponse> pondResponses = (fish.getPonds() != null)

                ? fish.getPonds().stream()
                .map(pond -> new PondWithFishResponse(pond.getPondId(),pond.getPondName()))
                .collect(Collectors.toList())
                : new ArrayList<>();

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
                fish.getPrice(),
                fishHistories,
                pondResponses

        );

    }

    public static FishDevelopmentHistory mapToFishHistory(AddFishDevelopmentHistoryRequest request) {
        return new FishDevelopmentHistory(
                request.getHistoryId(),
                request.getDate(),
                request.getSize(),
                request.getAge(),
                request.getWeight(),
                null
        );

    }

    public static KoiFishDevelopmentResponse mapToKoiFishResponse(FishDevelopmentHistory fishDevelopmentHistory) {
        return new KoiFishDevelopmentResponse(
                fishDevelopmentHistory.getHistoryId(),
                fishDevelopmentHistory.getSize(),
                fishDevelopmentHistory.getDate(),
                fishDevelopmentHistory.getAge(),
                fishDevelopmentHistory.getWeight(),
                fishDevelopmentHistory.getFish().getFishName()

        );

    }

//    public static FoodCalculationResponse mapToFoodCalculationResponse(FoodCalculation foodCalculation){
//
//        return new FoodCalculationResponse(
//                foodCalculation.getCalculationId(),
//                foodCalculation.getFeed(),
//                foodCalculation.getFoodAmount(),
//                foodCalculation.getFish().getFishName()
//        );
//    }
}
