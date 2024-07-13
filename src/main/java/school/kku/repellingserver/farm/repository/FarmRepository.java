package school.kku.repellingserver.farm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.farm.domain.Farm;

public interface FarmRepository extends JpaRepository<Farm, Long>, FarmRepositoryCustom {

}
