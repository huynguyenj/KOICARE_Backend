package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.AddWaterParameterRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.*;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.entity.WaterParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PondMapper {
    public static Pond mapToPond(AddPondRequest pondRequest){
        return new Pond(
                pondRequest.getPondId(),
                pondRequest.getPondName(),
                null,
                pondRequest.getSize(),
                pondRequest.getDepth(),
                pondRequest.getVolume(),
                pondRequest.getDrainCount(),
                pondRequest.getPumpCapacity(),
                new Date(),
                null,
                null,
                null

        );
    }
    public static PondResponse mapToPondResponse(Pond pond){

        List<FishResponseWithPond> fishes = (pond.getFish()!=null)
                ?pond.getFish().stream()
                .map(fish ->new FishResponseWithPond(
                        fish.getFishId(),
                        fish.getFishName(),
                        fish.getFishSize(),
                        fish.getFishShape(),
                        fish.getFishAge(),
                        fish.getFishWeight(),
                        fish.getFishGender(),
                        fish.getFishHealth(),
                        fish.getFishType(),
                        fish.getOrigin(),
                        fish.getPrice(),
                        fish.getOwner().getUserName())).collect(Collectors.toList()) : new ArrayList<>();

                        return new PondResponse(
                pond.getPondId(),
                pond.getPondName(),
                pond.getPondImg(),
                pond.getSize(),
                pond.getDepth(),
                pond.getVolume(),
                pond.getDrainCount(),
                pond.getPumpCapacity(),
                pond.getDate(),
                fishes, Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName())

        );
    }

    public static WaterParameter mapToWaterParameter(AddWaterParameterRequest request){
        return new WaterParameter(

            request.getParameterId(),
            request.getMeasurementTime(),
            request.getTemperature(),
            request.getSalinity(),
            request.getPh(),
            request.getO2(),
            request.getNo2(),
            request.getNo3(),
            request.getPo4(),
            null
        );
    }

    public static WaterParameterResponse mapToWaterParameterResponse(WaterParameter waterParameter){

        return new WaterParameterResponse(

                waterParameter.getParameterId(),
                waterParameter.getMeasurementTime(),
                waterParameter.getTemperature(),
                waterParameter.getSalinity(),
                waterParameter.getPh(),
                waterParameter.getO2(),
                waterParameter.getNo2(),
                waterParameter.getNo3(),
                waterParameter.getPo4(),
                waterParameter.getPond().getPondName()
        );
    }
}
