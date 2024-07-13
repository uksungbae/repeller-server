package school.kku.repellingserver.repellent.repellentDevice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.kku.repellingserver.gateway.dto.SerialIdExistResponse;
import school.kku.repellingserver.repellent.repellentDevice.service.RepellentDeviceService;

import java.nio.file.LinkOption;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/repellent-device")
public class RepellentDeviceController {

    private final RepellentDeviceService repellentDeviceService;

    @GetMapping("/valid/serial-id")
    public ResponseEntity<SerialIdExistResponse> validSerialId(@RequestParam String serialId, @RequestParam Long farmId) {
        boolean isSerialIdExists = repellentDeviceService.isSerialIdExistsActivated(serialId, farmId);

        return ResponseEntity.ok(SerialIdExistResponse.of(isSerialIdExists));
    }




}
