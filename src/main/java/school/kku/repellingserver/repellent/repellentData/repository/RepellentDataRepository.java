package school.kku.repellingserver.repellent.repellentData.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;

public interface RepellentDataRepository extends JpaRepository<RepellentData, Long> ,RepellentDataRepositoryCustom {

  Optional<RepellentData> findFirstByOrderByIdDesc();
}
