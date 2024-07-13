package school.kku.repellingserver.farm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.dto.FarmListResponse;
import school.kku.repellingserver.farm.dto.FarmRequest;
import school.kku.repellingserver.farm.service.FarmService;
import school.kku.repellingserver.member.domain.Member;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FarmController {

    private final FarmService farmService;

    @PostMapping("/farm")
    public ResponseEntity<List<Farm>> createFarm(@RequestBody FarmRequest farmRequest, @AuthenticationPrincipal Member member) {
        farmService.save(farmRequest, member);

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @GetMapping("/farm/setting/list")
    public ResponseEntity<List<FarmListResponse>> getSettingFarmList(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(farmService.getFarmListResponseList(member));
    }

    @GetMapping("/farm/list")
    public ResponseEntity<List<Farm>> getFarmList(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(farmService.getFarmList(member));
    }




}
