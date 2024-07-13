package school.kku.repellingserver.common.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.kku.repellingserver.farm.constant.FarmType;
import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.repository.FarmRepository;
import school.kku.repellingserver.gateway.domain.Gateway;
import school.kku.repellingserver.gateway.repository.GatewayRepository;
import school.kku.repellingserver.member.domain.Member;
import school.kku.repellingserver.member.repository.MemberRepository;
import school.kku.repellingserver.repellent.repellentData.domain.DetectionType;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentData;
import school.kku.repellingserver.repellent.repellentData.domain.RepellentSound;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentDataRepository;
import school.kku.repellingserver.repellent.repellentData.repository.RepellentSoundRepository;
import school.kku.repellingserver.repellent.repellentDevice.domain.RepellentDevice;
import school.kku.repellingserver.repellent.repellentDevice.repository.RepellentDeviceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostConstructService {

    private final GatewayRepository gatewayRepository;
    private final FarmRepository farmRepository;
    private final MemberRepository memberRepository;
    private final RepellentDeviceRepository repellentDeviceRepository;
    private final RepellentSoundRepository repellentSoundRepository;
    private final RepellentDataRepository repellentDataRepository;

    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .name("testName")
                .email("test@gmail.com")
                .loginId("test")
                .password(passwordEncoder.encode("test"))
                .build();
        memberRepository.save(member);

        Gateway gateway1 = Gateway.builder()
                .serialId("205")
                .build();
        gatewayRepository.save(gateway1);

        Gateway gateway2 = Gateway.builder()
                .serialId("206")
                .build();
        gatewayRepository.save(gateway2);

        Farm farm1 = Farm.builder()
                .name("test농장1")
                .farmType(FarmType.ONCHARD)
                .address("충주시 단월동")
                .member(member)
                .gateway(gateway1)
                .build();
        farmRepository.save(farm1);

        Farm farm2 = Farm.builder()
                .name("test농장2")
                .farmType(FarmType.RICE_FARM)
                .address("충주시 단월동 2")
                .member(member)
                .gateway(gateway2)
                .build();
        farmRepository.save(farm2);

        RepellentDevice repellentDevice1 = RepellentDevice.builder()
                .serialId("170")
                .name("1번기기")
                .latitude("37.5")
                .longitude("127.0")
                .farm(farm1)
                .isActivated(true)
                .isWorking(true)
                .build();
        repellentDeviceRepository.save(repellentDevice1);

        RepellentDevice repellentDevice2 = RepellentDevice.builder()
                .serialId("171")
                .name("2번기기")
                .latitude("37.5")
                .longitude("127.3")
                .farm(farm1)
                .isActivated(true)
                .isWorking(false)
                .build();
        repellentDeviceRepository.save(repellentDevice2);

        RepellentDevice repellentDevice3 = RepellentDevice.builder()
                .serialId("repellentDeviceId3")
                .name("3번기기")
                .latitude("37.5")
                .longitude("127.4")
                .farm(farm1)
                .isActivated(true)
                .isWorking(true)
                .build();
        repellentDeviceRepository.save(repellentDevice3);


        RepellentDevice repellentDevice4 = RepellentDevice.builder()
                .serialId("repellentDeviceId1")
                .name("4번기기")
                .latitude("37.0")
                .longitude("126.0")
                .farm(farm2)
                .isActivated(true)
                .isWorking(true)
                .build();
        repellentDeviceRepository.save(repellentDevice4);

        RepellentDevice repellentDevice5 = RepellentDevice.builder()
                .serialId("repellentDeviceId2")
                .name("5번기기")
                .latitude("37.0")
                .longitude("126.1")
                .farm(farm2)
                .isActivated(true)
                .isWorking(true)
                .build();
        repellentDeviceRepository.save(repellentDevice5);

        RepellentSound repellentSound = RepellentSound.builder()
                .soundLevel(1)
                .soundName("sound1")
                .build();
        repellentSoundRepository.save(repellentSound);

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(1)
                        .detectionDate(LocalDate.now().minusDays(5))
                        .detectionTime(LocalDateTime.now().minusDays(5))
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(2)
                        .detectionDate(LocalDate.now().minusDays(4))
                        .detectionTime(LocalDateTime.now().minusDays(4))
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(3)
                        .detectionDate(LocalDate.now().minusDays(3))
                        .detectionTime(LocalDateTime.now().minusDays(3))
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(4)
                        .detectionDate(LocalDate.now().minusDays(2))
                        .detectionTime(LocalDateTime.now().minusDays(2))
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(4)
                        .detectionDate(LocalDate.now().minusDays(1))
                        .detectionTime(LocalDateTime.now().minusDays(1))
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );

        repellentDataRepository.save(
                RepellentData.builder()
                        .detectionNum(10)
                        .detectionDate(LocalDate.now())
                        .detectionTime(LocalDateTime.now())
                        .detectionType(DetectionType.PIR)
                        .repellentSound(repellentSound)
                        .repellentDevice(repellentDevice1)
                        .build()
        );
    }
}
