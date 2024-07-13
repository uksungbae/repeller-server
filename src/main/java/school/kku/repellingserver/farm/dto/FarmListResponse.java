package school.kku.repellingserver.farm.dto;

import lombok.Data;

public record FarmListResponse (
    Long farmId,
    String farmName,
    Long deviceCount,
    String farmAddress
) {
    public static FarmListResponse of(Long farmId, String farmName, Long deviceCount, String farmAddress) {
        return new FarmListResponse(farmId, farmName, deviceCount, farmAddress);
    }
}
