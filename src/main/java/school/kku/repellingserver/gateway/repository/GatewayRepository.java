package school.kku.repellingserver.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.gateway.domain.Gateway;

import java.util.Optional;

public interface GatewayRepository extends JpaRepository<Gateway, Long> {

    boolean existsBySerialId(String serialId);
    boolean existsBySerialIdAndIsActivatedIsFalse(String serialId);

    Optional<Gateway> findBySerialId(String serialId);

}
