package school.kku.repellingserver.repellent.repellentData.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RepellentSound {

    @Id @GeneratedValue
    private Long id;

    private String soundName;

    private Integer soundLevel;

    @Builder
    public RepellentSound(Long id, String soundName, Integer soundLevel) {
        this.id = id;
        this.soundName = soundName;
        this.soundLevel = soundLevel;
    }
}
