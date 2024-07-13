package school.kku.repellingserver.gateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.kku.repellingserver.gateway.dto.RepellentDataRequest;
import school.kku.repellingserver.gateway.dto.SerialIdExistResponse;
import school.kku.repellingserver.gateway.service.GatewayService;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    private final GatewayService gatewayService;

//    FIXME : SerialId를 관리자가 미리 삽입 필요
    @GetMapping("/gateway/valid/serial-id")
    public ResponseEntity<SerialIdExistResponse> validSerialId(@RequestParam String serialId) {

        boolean isSerialIdExists = gatewayService.isSerialIdExists(serialId);

        return ResponseEntity.ok(SerialIdExistResponse.of(isSerialIdExists));
    }

}
