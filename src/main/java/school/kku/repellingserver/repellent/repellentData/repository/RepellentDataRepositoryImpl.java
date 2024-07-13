package school.kku.repellingserver.repellent.repellentData.repository;

import static school.kku.repellingserver.repellent.repellentData.domain.QRepellentData.repellentData;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.kku.repellingserver.repellent.repellentData.dto.DailyDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.DayByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.HourByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.MainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.QMainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.ReDetectionMinutesAndRepellentSoundResponse;

@RequiredArgsConstructor
public class RepellentDataRepositoryImpl implements RepellentDataRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  LocalDate fourDaysAgo = LocalDate.now().minusDays(4);
  LocalDate now = LocalDate.now();

  @Override
  public List<DayByDetectionListResponse> findRepellentDataByMemberWithDays(Long farmId) {
    return jpaQueryFactory.select(
            Projections.constructor(
                DayByDetectionListResponse.class,
                repellentData.detectionDate,
                repellentData.detectionType,
                repellentData.detectionNum.sum()
            )
        )
        .from(repellentData)
        .where(
            repellentData.repellentDevice.farm.id.eq(farmId)
                .and(repellentData.detectionDate.between(fourDaysAgo, now))
        )
        .groupBy(repellentData.detectionDate, repellentData.detectionType)
        .orderBy(repellentData.detectionDate.desc()) // 최신 날짜 순으로 정렬
        .fetch();
  }

  @Override
  public MainPageDataResponse findRepellentDataByFarmGroupByRepellentSound(Long farmId) {
    return jpaQueryFactory.select(
            new QMainPageDataResponse(
                repellentData.reDetectionMinutes.avg(),
                repellentData.repellentSound.soundName
            )
        )
        .from(repellentData)
        .where(
            repellentData.repellentDevice.farm.id.eq(farmId)
                .and(repellentData.detectionDate.between(fourDaysAgo, now))
        )
        .groupBy(repellentData.repellentSound.soundName)
        .orderBy(repellentData.reDetectionMinutes.avg().desc())
        .fetchOne();
  }

  @Override
  public List<HourByDetectionListResponse> findRepellentDataByMemberWithHoursBetweenTime(
      Long farmId, LocalDateTime start, LocalDateTime end) {

    return jpaQueryFactory.select(
            Projections.constructor(
                HourByDetectionListResponse.class,
                repellentData.detectionTime,
                repellentData.detectionType,
                repellentData.detectionNum.sum()
            )
        )
        .from(repellentData)
        .where(
            repellentData.repellentDevice.farm.id.eq(farmId)
                .and(repellentData.detectionTime.between(start, end))
        )
        .groupBy(repellentData.detectionTime, repellentData.detectionType)
        .fetch();
  }

  @Override
  public List<DailyDetectionListResponse> getDailyStatsForDevice(Long farmId) {
    return jpaQueryFactory
        .select(
            Projections.constructor(
                DailyDetectionListResponse.class,
                repellentData.repellentDevice.name,
                repellentData.detectionType,
                repellentData.detectionNum.sum()
            )
        )
        .from(repellentData)
        .where(
            repellentData.repellentDevice.farm.id.eq(farmId),
            repellentData.detectionDate.eq(now)
        )
        .groupBy(
            repellentData.repellentDevice.id,
            repellentData.repellentDevice.name,
            repellentData.detectionType
        ).fetch();
  }

  @Override
  public List<DayByDetectionListResponse> findEachDayDetectionListByDetectionDeviceId(
      Long detectionDeviceId) {
    return jpaQueryFactory.select(
            Projections.constructor(
                DayByDetectionListResponse.class,
                repellentData.detectionDate,
                repellentData.detectionType,
                repellentData.detectionNum.sum()
            )
        )
        .from(repellentData)
        .where(
            repellentData.repellentDevice.id.eq(detectionDeviceId)
                .and(repellentData.detectionDate.between(fourDaysAgo, now))
        )
        .groupBy(repellentData.detectionDate, repellentData.detectionType)
        .orderBy(repellentData.detectionDate.desc()) // 최신 날짜 순으로 정렬
        .fetch();
  }

  @Override
  public List<ReDetectionMinutesAndRepellentSoundResponse> findDetectionListByDetectionDeviceIdLimit4(
      Long detectionDeviceId) {

    return jpaQueryFactory
        .select(
            Projections.constructor(
                ReDetectionMinutesAndRepellentSoundResponse.class,
                repellentData.detectionTime,
                repellentData.reDetectionMinutes,
                repellentData.repellentSound.soundName
            )
        ).from(repellentData)
        .where(
            repellentData.repellentDevice.id.eq(detectionDeviceId)
        ).orderBy(repellentData.id.desc())
        .limit(4)
        .fetch();
  }


}
