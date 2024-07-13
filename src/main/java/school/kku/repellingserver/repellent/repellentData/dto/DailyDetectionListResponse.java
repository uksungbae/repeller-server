package school.kku.repellingserver.repellent.repellentData.dto;

public record DailyDetectionListResponse(
    String repellentDeviceName,
    String detectionType,
    Long detectionNumSum
) {
    public static DailyDetectionListResponse of(String repellentDeviceName, String detectionType, Long detectionNumSum) {
        return new DailyDetectionListResponse(repellentDeviceName, detectionType, detectionNumSum);
    }
}
