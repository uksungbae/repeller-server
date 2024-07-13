package school.kku.repellingserver.gateway.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Gateway {

    @Id @GeneratedValue
    private Long id;

    private String serialId;

    @Setter
    private String ipv4;

    @ColumnDefault("false")
    private Boolean isActivated;

    @Builder
    public Gateway(Long id, String serialId, String ipv4, Boolean isActivated) {
        this.id = id;
        this.serialId = serialId;
        this.ipv4 = ipv4;
        this.isActivated = isActivated;
    }
}
