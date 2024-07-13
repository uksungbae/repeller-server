package school.kku.repellingserver.repellent.repellentData.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.kku.repellingserver.gateway.dto.RepellentDataRequest;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;
import school.kku.repellingserver.repellent.repellentData.dto.DailyDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.DayByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.HourByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.MainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.ReDetectionMinutesAndRepellentSoundResponse;
import school.kku.repellingserver.repellent.repellentData.service.RepellentDataService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/repellent-data")
public class RepellentDataController {

  private final RepellentDataService repellentDataService;

  // FIXME: 현재 Lora gateway serialId :  205
  @PostMapping
  public ResponseEntity<String> repellentData(@RequestBody RepellentDataRequest request,
      HttpServletRequest httpServletRequest) {
    String gatewayIp = httpServletRequest.getRemoteAddr();
    log.info("gateway IP : {}", gatewayIp);
    RepellentData repellentData = repellentDataService.saveData(request, gatewayIp);
    log.info("repellentData : {}", repellentData);

    return ResponseEntity.ok("OK");
  }

  @GetMapping("/main")
  public ResponseEntity<MainPageDataResponse> getRepellentDataList(@RequestParam Long farmId) {
    return ResponseEntity.ok(repellentDataService.getRepellentDataList(farmId));
  }

  @GetMapping("/detail/group-farm/farm/{farmId}")
  public ResponseEntity<List<DayByDetectionListResponse>> getDayByDetectionList(@PathVariable Long farmId) {
    return ResponseEntity.ok(repellentDataService.getDayByDetectionList(farmId));
  }

  @GetMapping("/detail/group-time/farm/{farmId}")
  public ResponseEntity<List<HourByDetectionListResponse>> getDayByTimeList(@PathVariable Long farmId) {
    return ResponseEntity.ok(repellentDataService.getDayByTimeList(farmId));
  }

  @GetMapping("/detail/group-detection-device/farm/{farmId}")
  public ResponseEntity<List<DailyDetectionListResponse>> getGroupByDetectionDevice(@PathVariable Long farmId) {
    return ResponseEntity.ok(repellentDataService.getDailyByDetectionList(farmId));
  }

  @GetMapping("/detail/detection-device-id/{detectionDeviceId}")
  public ResponseEntity<List<DayByDetectionListResponse>> getEachDayDetectionListByDetectionDeviceId(@PathVariable Long detectionDeviceId) {
    return ResponseEntity.ok(repellentDataService.getEachDayDetectionListByDetectionDeviceId(detectionDeviceId));
  }

  @GetMapping("/detail/detection/detection-device-id/{detectionDeviceId}")
  public ResponseEntity<List<ReDetectionMinutesAndRepellentSoundResponse>> getDetectionListByDetectionDeviceId(@PathVariable Long detectionDeviceId) {
    return ResponseEntity.ok(repellentDataService.getDetectionListByDetectionDeviceId(detectionDeviceId));
  }

}
