package school.kku.repellingserver.repellent.repellentData.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class MainPageDataResponse {

  @Setter
  private List<DayByDetectionListResponse> dayByDetectionList;
  private Long reDetectionTimeAvg;
  private String repellentSoundName;

  public static MainPageDataResponse of(
      List<DayByDetectionListResponse> dayByDetectionListResponseList,
      Long reDetectionTimeAvg, String detectionName) {
    return new MainPageDataResponse(dayByDetectionListResponseList, reDetectionTimeAvg,
        detectionName);
  }

  public MainPageDataResponse(List<DayByDetectionListResponse> dayByDetectionList,
      Long reDetectionTimeAvg, String repellentSoundName) {
    this.dayByDetectionList = dayByDetectionList;
    this.reDetectionTimeAvg = reDetectionTimeAvg;
    this.repellentSoundName = repellentSoundName;
  }

  @QueryProjection
  public MainPageDataResponse(Double reDetectionTimeAvg, String repellentSoundName) {
    this.reDetectionTimeAvg = reDetectionTimeAvg.longValue();
    this.repellentSoundName = repellentSoundName;
  }
}
