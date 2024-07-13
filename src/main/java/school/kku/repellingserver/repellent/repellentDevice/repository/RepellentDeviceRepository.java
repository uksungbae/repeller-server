package school.kku.repellingserver.repellent.repellentDevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;

import java.util.Optional;

public interface RepellentDeviceRepository extends JpaRepository<RepellentDevice, Long> {

    Optional<RepellentDevice> findBySerialId(String serialId);

    boolean existsBySerialIdAndIsActivatedIsFalse(String serialId);
}
