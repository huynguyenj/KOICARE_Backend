package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddWaterParameterRequest;
import group7.se1876.kcs_backend.dto.request.WaterParameterUpdateRequest;
import group7.se1876.kcs_backend.dto.response.WaterParameterHistoryResponse;
import group7.se1876.kcs_backend.dto.response.WaterParameterResponse;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.PondWaterPramHistory;
import group7.se1876.kcs_backend.entity.WaterParameter;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.PondMapper;
import group7.se1876.kcs_backend.repository.FishRepository;
import group7.se1876.kcs_backend.repository.PondRepository;
import group7.se1876.kcs_backend.repository.WaterHistoryRepository;
import group7.se1876.kcs_backend.repository.WaterParameterRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WaterParameterService {

    private PondRepository pondRepository;
    private WaterParameterRepository waterParameterRepository;
    private PondMapper pondMapper;
    private WaterHistoryRepository waterHistoryRepository;

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

        PondWaterPramHistory pondWaterPramHistory = pondMapper.mapToWaterParameterHistory(request);
        pondWaterPramHistory.setPond(pond);
        pond.getPondWaterParamsHistory().add(pondWaterPramHistory);
        waterHistoryRepository.save(pondWaterPramHistory);


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
            recommendations.put("RESOLVE_TEMPERATURE", "Bạn nên để nhiệt độ ở giữa " + IDEAL_TEMPERATURE_MIN + " và " + IDEAL_TEMPERATURE_MAX + " °C.Sử dụng máy điều chỉnh nhiệt độ để ổn định nhiệt độ cho hồ.");
        }

        // Check salinity
        if (waterParameter.getSalinity() < IDEAL_SALINITY_MIN || waterParameter.getSalinity() > IDEAL_SALINITY_MAX) {
            recommendations.put("RESOLVE_SALINITY", "Bạn nên điều chỉnh độ mặn ở giữa " + IDEAL_SALINITY_MIN + " và " + IDEAL_SALINITY_MAX + " ppt.Sử dụng muối khoáng đặc biệt cho hồ koi để điều chỉnh độ mặn.");
        }

        // Check pH
        if (waterParameter.getPh() < IDEAL_PH_MIN || waterParameter.getPh() > IDEAL_PH_MAX) {
            recommendations.put("RESOLVE_PH", "Bạn nên điều chỉnh độ pH giữa khoảng " + IDEAL_PH_MIN + " và " + IDEAL_PH_MAX + ".Bạn có thể sử dụng dung dịch điều chỉnh pH cho hồ koi.");
        }

        // Check O2
        if (waterParameter.getO2() < IDEAL_O2_MIN || waterParameter.getO2() > IDEAL_O2_MAX) {
            recommendations.put("RESOLVE_O2", "Bạn nên điều chỉnh nồng độ O2 giữa khoảng " + IDEAL_O2_MIN + " và " + IDEAL_O2_MAX + " mg/L.Bạn có thể dùng máy sục khí sẽ giúp tăng cường nồng độ oxy.");
        }

        // Check NO2
        if (waterParameter.getNo2() > IDEAL_NO2_MAX) {
            recommendations.put("RESOLVE_NO2", "Nồng độ NO2 nên ở  0.0 mg/L. Hãy thực hiện quy trình giảm lại lượng NO2. Hãy sử dụng bộ lọc sinh học để giảm lượng NO2.");
        }

        // Check NO3
        if (waterParameter.getNo3() > IDEAL_NO3_MAX) {
            recommendations.put("RESOLVE_NO3", "Bạn nên điều chỉnh nồng độ NO3 dưới " + IDEAL_NO3_MAX + " mg/L.Sử dụng cây thủy sinh hoặc bộ lọc NO3 để giảm nồng độ NO3.");
        }

        // Check PO4
        if (waterParameter.getPo4() > IDEAL_PO4_MAX) {
            recommendations.put("RESOLVE_PO4", "Bạn nên điều chỉnh nồng độ PO4 dưới " + IDEAL_PO4_MAX + " mg/L.Bộ lọc PO4 sẽ giúp ổn định nồng độ PO4.");
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

        PondWaterPramHistory pondWaterPramHistory = new PondWaterPramHistory();
        pondWaterPramHistory.setMeasurementTime(request.getMeasurementTime());
        pondWaterPramHistory.setTemperature(request.getTemperature());
        pondWaterPramHistory.setSalinity(request.getSalinity());
        pondWaterPramHistory.setPh(request.getPh());
        pondWaterPramHistory.setO2(request.getO2());
        pondWaterPramHistory.setNo2(request.getNo2());
        pondWaterPramHistory.setNo3(request.getNo3());
        pondWaterPramHistory.setPo4(request.getPo4());
        pondWaterPramHistory.setPond(pond);
        pond.getPondWaterParamsHistory().add(pondWaterPramHistory);
        waterHistoryRepository.save(pondWaterPramHistory);

        return pondMapper.mapToWaterParameterResponse(waterParameter);

    }

    //Salt calculation
    public Double saltCalculation(Long pondId, String saltPercent){

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        double volume = pond.getVolume();
        System.out.println(volume);
        double result = 0;

        switch (saltPercent){
            case "0.3":
                result =  volume*0.003;
                break;
            case "0.5":
                result =  volume*0.005;
                break;
            case "0.7":
                result =  volume*0.007;
                break;
            default:
                result = 0;
        }
        return result;
    }


    public List<WaterParameterHistoryResponse> getAllHistoryWaterParam(Long pondId){
        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_EXISTED));
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userId != pond.getUser().getUserId()){
            throw new AppException(ErrorCode.INVALID_DATA_WITH_USERID);
        }

        List<PondWaterPramHistory> pondWaterPramHistories = pond.getPondWaterParamsHistory();

        return pondWaterPramHistories.stream().map((history)
                -> pondMapper.mapToWaterParamHistoryResponse(history)).collect(Collectors.toList());

    }

}


