package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddWaterParameterRequest;
import group7.se1876.kcs_backend.dto.request.WaterParameterUpdateRequest;
import group7.se1876.kcs_backend.dto.response.WaterParameterResponse;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.WaterParameter;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.PondMapper;
import group7.se1876.kcs_backend.repository.FishRepository;
import group7.se1876.kcs_backend.repository.PondRepository;
import group7.se1876.kcs_backend.repository.WaterParameterRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class WaterParameterService {

    private PondRepository pondRepository;
    private WaterParameterRepository waterParameterRepository;
    private PondMapper pondMapper;


    private static final double IDEAL_TEMPERATURE_MIN = 20.0;
    private static final double IDEAL_TEMPERATURE_MAX = 25.0;

    private static final double IDEAL_SALINITY_MIN = 0.0;
    private static final double IDEAL_SALINITY_MAX = 0.2;

    private static final double IDEAL_PH_MIN = 7.0;
    private static final double IDEAL_PH_MAX = 8.0;

    private static final double IDEAL_O2_MIN = 5.0;
    private static final double IDEAL_O2_MAX = 8.0;

    private static final double IDEAL_NO2_MAX = 0.0;

    private static final double IDEAL_NO3_MAX = 40.0;

    private static final double IDEAL_PO4_MAX = 1.0;
    //Add water parameter for pond
    public WaterParameterResponse addWaterParameterForPond(Long pondId, AddWaterParameterRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_USERID);
        }

        if (waterParameterRepository.existsByPond_PondId(pondId)){
            throw new AppException(ErrorCode.INVALID_INFOMATION);
        }

        WaterParameter waterParameter = pondMapper.mapToWaterParameter(request);
        waterParameter.setPond(pond);
        waterParameterRepository.save(waterParameter);

        return pondMapper.mapToWaterParameterResponse(waterParameter);
    }

    //Check water parameter
    public Map<String, String> checkWaterParameters(Long pondId) {

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        WaterParameter waterParameter = waterParameterRepository.findByPond_PondId(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        Map<String, String> recommendations = new HashMap<>();


        // Check temperature
        if ( waterParameter.getTemperature()< IDEAL_TEMPERATURE_MIN || waterParameter.getTemperature() > IDEAL_TEMPERATURE_MAX) {
            recommendations.put("Temperature", "Adjust temperature to between " + IDEAL_TEMPERATURE_MIN + " and " + IDEAL_TEMPERATURE_MAX + " °C.");
        }

        // Check salinity
        if (waterParameter.getSalinity() < IDEAL_SALINITY_MIN || waterParameter.getSalinity() > IDEAL_SALINITY_MAX) {
            recommendations.put("Salinity", "Adjust salinity to between " + IDEAL_SALINITY_MIN + " and " + IDEAL_SALINITY_MAX + " ppt.");
        }

        // Check pH
        if (waterParameter.getPh() < IDEAL_PH_MIN || waterParameter.getPh() > IDEAL_PH_MAX) {
            recommendations.put("pH", "Adjust pH to between " + IDEAL_PH_MIN + " and " + IDEAL_PH_MAX + ".");
        }

        // Check O2
        if (waterParameter.getO2() < IDEAL_O2_MIN || waterParameter.getO2() > IDEAL_O2_MAX) {
            recommendations.put("O2", "Adjust O2 levels to between " + IDEAL_O2_MIN + " and " + IDEAL_O2_MAX + " mg/L.");
        }

        // Check NO2
        if (waterParameter.getNo2() > IDEAL_NO2_MAX) {
            recommendations.put("NO2", "NO2 levels should be 0.0 mg/L. Take measures to reduce NO2.");
        }

        // Check NO3
        if (waterParameter.getNo3() > IDEAL_NO3_MAX) {
            recommendations.put("NO3", "Adjust NO3 levels to below " + IDEAL_NO3_MAX + " mg/L.");
        }

        // Check PO4
        if (waterParameter.getPo4() > IDEAL_PO4_MAX) {
            recommendations.put("PO4", "Adjust PO4 levels to below " + IDEAL_PO4_MAX + " mg/L.");
        }

        return recommendations;
    }

    //Get water param
    public WaterParameterResponse getWaterParameter(Long pondId){

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        WaterParameter waterParameter = waterParameterRepository.findByPond_PondId(pondId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        return pondMapper.mapToWaterParameterResponse(waterParameter);

    }

//    Update water parameter
    public WaterParameterResponse updateWaterParameter(Long pondId,WaterParameterUpdateRequest request){

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        WaterParameter waterParameter = waterParameterRepository.findByPond_PondId(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));

        waterParameter.setMeasurementTime(request.getMeasurementTime());
        waterParameter.setTemperature(request.getTemperature());
        waterParameter.setSalinity(request.getSalinity());
        waterParameter.setPh(request.getPh());
        waterParameter.setO2(request.getO2());
        waterParameter.setNo2(request.getNo2());
        waterParameter.setNo3(request.getNo3());
        waterParameter.setPo4(request.getPo4());

        waterParameterRepository.save(waterParameter);

        return pondMapper.mapToWaterParameterResponse(waterParameter);

    }
}


