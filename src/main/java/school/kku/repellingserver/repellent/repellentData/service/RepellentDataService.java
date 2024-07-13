package school.kku.repellingserver.repellent.repellentData.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.kku.repellingserver.gateway.domain.Gateway;
import school.kku.repellingserver.gateway.dto.RepellentDataRequest;
import school.kku.repellingserver.gateway.repository.GatewayRepository;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentSound;
import school.kku.repellingserver.repellent.repellentData.dto.DailyDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.DayByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.HourByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.MainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.ReDetectionMinutesAndRepellentSoundResponse;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentDataRepository;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentSoundRepository;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;
import school.kku.repellingserver.repellent.repellentDevice.repository.RepellentDeviceRepository;

@RequiredArgsConstructor
@Service
public class RepellentDataService {

    private final RepellentDataRepository repellentDataRepository;
    private final GatewayRepository gatewayRepository;
    private final RepellentSoundRepository repellentSoundRepository;
    private final RepellentDeviceRepository repellentDeviceRepository;

    public MainPageDataResponse getRepellentDataList(Long farmId) {

        List<DayByDetectionListResponse> dayByDataList = repellentDataRepository.findRepellentDataByMemberWithDays(
            farmId);

        MainPageDataResponse response = repellentDataRepository.findRepellentDataByFarmGroupByRepellentSound(
            farmId);

        response.setDayByDetectionList(dayByDataList);

        return response;
    }

    @Transactional
    public RepellentData saveData(RepellentDataRequest request, String gatewayIp) {

        boolean isGatewayExists = gatewayRepository.existsBySerialId(request.gatewayId());

        if (isGatewayExists) {
            gatewayRepository.findBySerialId(request.gatewayId())
                .ifPresent(gateway -> {
                    gateway.setIpv4(gatewayIp);
                });

        } else {
            gatewayRepository.save(
                Gateway.builder()
                    .serialId(request.gatewayId())
                    .ipv4(gatewayIp)
                    .build()
            );

        }

        boolean isRepellentSoundExists = repellentSoundRepository.existsBySoundNameAndSoundLevel(request.soundType(), request.soundLevel());

        if (isRepellentSoundExists) {
            RepellentSound repellentSound = repellentSoundRepository.findBySoundNameAndSoundLevel(request.soundType(), request.soundLevel()).get();
            RepellentDevice repellentDevice = repellentDeviceRepository.findBySerialId(request.nodeId())
                .orElseThrow(() -> new RuntimeException("Repellent Device not found"));

            RepellentData recentRepellentData = repellentDataRepository.findFirstByOrderByIdDesc()
                .orElse(RepellentData.builder().detectionTime(request.timestamp()).build());

            Duration duration = Duration.between(request.timestamp(),
                recentRepellentData.getDetectionTime());

            return repellentDataRepository.save(
                RepellentData.builder()
                    .detectionType(request.detectionType())
                    .detectionTime(request.timestamp())
                    .detectionDate(request.timestamp().toLocalDate())
                    .repellentDevice(repellentDevice)
                    .repellentSound(repellentSound)
                    .reDetectionMinutes(duration.toMinutes())
                    .detectionNum(request.detectedCount())
                    .build()
            );
        } else {

            RepellentSound repellentSound = repellentSoundRepository.save(RepellentSound.builder()
                .soundName(request.soundType())
                .soundLevel(request.soundLevel())
                .build()
            );
            RepellentDevice repellentDevice = repellentDeviceRepository.findBySerialId(request.nodeId())
                .orElseThrow(() -> new RuntimeException("Repellent Device not found"));

            RepellentData recentRepellentData = repellentDataRepository.findFirstByOrderByIdDesc()
                .orElse(RepellentData.builder().detectionTime(request.timestamp()).build());

            Duration duration = Duration.between(request.timestamp(),
                recentRepellentData.getDetectionTime());

            return repellentDataRepository.save(
                RepellentData.builder()
                    .detectionType(request.detectionType())
                    .detectionTime(request.timestamp())
                    .detectionDate(request.timestamp().toLocalDate())
                    .repellentDevice(repellentDevice)
                    .repellentSound(repellentSound)
                    .reDetectionMinutes(duration.toMinutes())
                    .detectionNum(request.detectedCount())
                    .build()
            );
        }
    }

    public List<DayByDetectionListResponse> getDayByDetectionList(Long farmId) {
        return repellentDataRepository.findRepellentDataByMemberWithDays(farmId);
    }

    public List<HourByDetectionListResponse> getDayByTimeList(Long farmId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursAgo = now.minusHours(2);
        LocalDateTime fourHoursAgo = now.minusHours(4);
        LocalDateTime sixHoursAgo = now.minusHours(6);
        LocalDateTime eightHoursAgo = now.minusHours(8);

        List<HourByDetectionListResponse> response = repellentDataRepository.findRepellentDataByMemberWithHoursBetweenTime(
            farmId, now, twoHoursAgo);
        List<HourByDetectionListResponse> response1 = repellentDataRepository.findRepellentDataByMemberWithHoursBetweenTime(
            farmId, twoHoursAgo, fourHoursAgo);
        List<HourByDetectionListResponse> response2 = repellentDataRepository.findRepellentDataByMemberWithHoursBetweenTime(
            farmId, fourHoursAgo, sixHoursAgo);
        List<HourByDetectionListResponse> response3 = repellentDataRepository.findRepellentDataByMemberWithHoursBetweenTime(
            farmId, sixHoursAgo, eightHoursAgo);

        response.addAll(response1);
        response.addAll(response2);
        response.addAll(response3);
        return response;
    }

    public List<DailyDetectionListResponse> getDailyByDetectionList(Long farmId) {
        return repellentDataRepository.getDailyStatsForDevice(
            farmId);
    }

    public List<DayByDetectionListResponse> getEachDayDetectionListByDetectionDeviceId(Long detectionDeviceId) {
        return repellentDataRepository.findEachDayDetectionListByDetectionDeviceId(detectionDeviceId);
    }

    public List<ReDetectionMinutesAndRepellentSoundResponse> getDetectionListByDetectionDeviceId(Long detectionDeviceId) {
        return repellentDataRepository.findDetectionListByDetectionDeviceIdLimit4(detectionDeviceId);
    }

}
