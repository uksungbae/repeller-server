package school.kku.repellingserver.repellent.repellentData.dto;

import school.kku.repellingserver.repellent.repellentData.domain.DetectionType;

import java.time.LocalDate;


public record DayByDetectionListResponse(
    LocalDate detectedAt,
    DetectionType detectionType,
    Integer count
) {
  public static DayByDetectionListResponse of(LocalDate detectedAt, DetectionType detectionType,
      Integer count) {
    return new DayByDetectionListResponse(detectedAt, detectionType, count);
  }
}
