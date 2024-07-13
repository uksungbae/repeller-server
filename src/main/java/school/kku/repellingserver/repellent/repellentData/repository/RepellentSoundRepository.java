package school.kku.repellingserver.repellent.repellentData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentSound;

import java.util.Optional;

public interface RepellentSoundRepository extends JpaRepository<RepellentSound, Long> {

    Optional<RepellentSound> findBySoundNameAndSoundLevel(String soundName, Integer soundLevel);
    boolean existsBySoundNameAndSoundLevel(String soundName, Integer soundLevel);


}
