package school.kku.repellingserver.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import school.kku.repellingserver.repellent.repellentData.domain.DetectionType;

public record RepellentDataRequest(
    String gatewayId,
    String nodeId,
    String message,
    String soundType,
    Integer soundLevel,
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd,HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime timestamp,
    DetectionType detectionType, Integer detectedCount) {
  public static RepellentDataRequest of(String gatewayId, String nodeId, String message,
      String soundType, Integer soundLevel, LocalDateTime timestamp, DetectionType detectionType,
      Integer detectedCount) {
    return new RepellentDataRequest(gatewayId, nodeId, message, soundType, soundLevel, timestamp,
        detectionType, detectedCount);
  }
}
