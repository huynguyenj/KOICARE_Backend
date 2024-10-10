package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.FishResponse;
import group7.se1876.kcs_backend.dto.response.FishResponseWithPond;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.dto.response.RoleRespone;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.User;
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
                pondRequest.getPondImg(),
                pondRequest.getSize(),
                pondRequest.getDepth(),
                pondRequest.getVolume(),
                pondRequest.getDrainCount(),
                pondRequest.getPumpCapacity(),
                pondRequest.getSaltAmount(),
                new Date(),
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
                pond.getSaltAmount(),
                pond.getDate(),
                fishes, Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName())

        );
    }
}
