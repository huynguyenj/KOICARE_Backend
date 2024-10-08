package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.PondUpdateRequest;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.entity.Pond;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

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
                pond.getDate()

        );
    }
}
