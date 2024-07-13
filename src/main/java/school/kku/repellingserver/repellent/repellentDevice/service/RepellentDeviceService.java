package school.kku.repellingserver.repellent.repellentDevice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.repository.FarmRepository;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;
import school.kku.repellingserver.repellent.repellentDevice.repository.RepellentDeviceRepository;

@RequiredArgsConstructor
@Service
public class RepellentDeviceService {

    private final RepellentDeviceRepository repellentDeviceRepository;
    private final FarmRepository farmRepository;

    @Transactional(readOnly = true)
    public boolean isSerialIdExistsActivated(String serialId, Long farmId) {
        boolean isActivate = repellentDeviceRepository.existsBySerialIdAndIsActivatedIsFalse(serialId);

        if (!isActivate) {
            return false;
        } else {
            RepellentDevice repellentDevice = repellentDeviceRepository.findBySerialId(serialId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시리얼 아이디입니다."));
            repellentDevice.activate();

            Farm farm = farmRepository.findById(farmId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 농장입니다."));

            repellentDevice.setFarm(farm);
            return true;
        }
    }
}
