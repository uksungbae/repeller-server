package school.kku.repellingserver.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.kku.repellingserver.gateway.domain.Gateway;
import school.kku.repellingserver.gateway.dto.RepellentDataRequest;
import school.kku.repellingserver.gateway.repository.GatewayRepository;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentSound;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentDataRepository;
import school.kku.repellingserver.repellent.repellentDevice.repository.RepellentDeviceRepository;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentSoundRepository;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;

@RequiredArgsConstructor
@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;
    private final RepellentDataRepository repellentDataRepository;
    private final RepellentSoundRepository repellentSoundRepository;
    private final RepellentDeviceRepository repellentDeviceRepository;

    public boolean isSerialIdExists(String serialId) {
        return gatewayRepository.existsBySerialIdAndIsActivatedIsFalse(serialId);
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

            return repellentDataRepository.save(
                    RepellentData.builder()
                            .detectionType(request.detectionType())
                            .detectionTime(request.timestamp())
                            .detectionDate(request.timestamp().toLocalDate())
                            .repellentDevice(repellentDevice)
                            .repellentSound(repellentSound)
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

            return repellentDataRepository.save(
                    RepellentData.builder()
                            .detectionType(request.detectionType())
                            .detectionTime(request.timestamp())
                            .detectionDate(request.timestamp().toLocalDate())
                            .repellentDevice(repellentDevice)
                            .repellentSound(repellentSound)
                            .detectionNum(request.detectedCount())
                            .build()
            );
        }
    }
}
