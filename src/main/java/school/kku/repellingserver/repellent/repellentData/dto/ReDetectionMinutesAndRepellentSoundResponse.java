package school.kku.repellingserver.repellent.repellentData.dto;

import java.time.LocalTime;

public record ReDetectionMinutesAndRepellentSoundResponse(
    LocalTime detectionTime,
    Integer reDetectionMinutes,
    String repellentSound
) {
  public static ReDetectionMinutesAndRepellentSoundResponse of(LocalTime detectionTime,Integer reDetectionMinutes, String repellentSound) {
    return new ReDetectionMinutesAndRepellentSoundResponse(detectionTime,reDetectionMinutes, repellentSound);
  }

}
