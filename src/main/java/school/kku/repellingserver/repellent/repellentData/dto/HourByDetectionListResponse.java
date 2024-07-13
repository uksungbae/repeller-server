package school.kku.repellingserver.repellent.repellentData.dto;

import java.time.LocalTime;
import school.kku.repellingserver.repellent.repellentData.domain.DetectionType;


public record HourByDetectionListResponse(
    LocalTime detectedAt,
    DetectionType detectionType,
    Integer count
) {
  public static HourByDetectionListResponse of(LocalTime detectedAt, DetectionType detectionType,
      Integer count) {
    return new HourByDetectionListResponse(detectedAt, detectionType, count);
  }
}
