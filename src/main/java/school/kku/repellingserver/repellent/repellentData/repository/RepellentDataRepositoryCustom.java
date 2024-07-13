package school.kku.repellingserver.repellent.repellentData.repository;

import java.time.LocalDateTime;
import java.util.List;
import school.kku.repellingserver.repellent.repellentData.dto.DailyDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.DayByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.HourByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.MainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.ReDetectionMinutesAndRepellentSoundResponse;

public interface RepellentDataRepositoryCustom {

  List<DayByDetectionListResponse> findRepellentDataByMemberWithDays(Long farmId);

  MainPageDataResponse findRepellentDataByFarmGroupByRepellentSound(Long farmId);

  List<HourByDetectionListResponse> findRepellentDataByMemberWithHoursBetweenTime(Long farmId,
      LocalDateTime start, LocalDateTime end);

  List<DailyDetectionListResponse> getDailyStatsForDevice(Long farmId);

  List<DayByDetectionListResponse> findEachDayDetectionListByDetectionDeviceId(Long detectionDeviceId);

  List<ReDetectionMinutesAndRepellentSoundResponse> findDetectionListByDetectionDeviceIdLimit4(Long detectionDeviceId);
}
