package school.kku.repellingserver.repellent.repellentData.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString(exclude = {"repellentDevice", "repellentSound"})
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RepellentData {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DetectionType detectionType;

//    @Column(columnDefinition = "int default 0")
    @ColumnDefault("0")
    private Integer detectionNum;

    private LocalDateTime detectionTime;

    private LocalDate detectionDate;

    @ColumnDefault("0")
    private Long reDetectionMinutes;

    @ManyToOne(fetch = FetchType.EAGER)
    private RepellentDevice repellentDevice;

    @ManyToOne(fetch = FetchType.EAGER)
    private RepellentSound repellentSound;

    @Builder
    public RepellentData(Long id, DetectionType detectionType, Integer detectionNum,
        LocalDateTime detectionTime, LocalDate detectionDate, Long reDetectionMinutes,
        RepellentDevice repellentDevice, RepellentSound repellentSound) {
        this.id = id;
        this.detectionType = detectionType;
        this.detectionNum = detectionNum;
        this.detectionTime = detectionTime;
        this.detectionDate = detectionDate;
        this.reDetectionMinutes = reDetectionMinutes;
        this.repellentDevice = repellentDevice;
        this.repellentSound = repellentSound;
    }
}
