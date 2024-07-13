package school.kku.repellingserver.farm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.dto.FarmListResponse;
import school.kku.repellingserver.farm.dto.FarmRequest;
import school.kku.repellingserver.farm.repository.FarmRepository;
import school.kku.repellingserver.gateway.domain.Gateway;
import school.kku.repellingserver.gateway.repository.GatewayRepository;
import school.kku.repellingserver.member.domain.Member;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FarmService {

    private final FarmRepository farmRepository;
    private final GatewayRepository gatewayRepository;


    @Transactional
    public Farm save(FarmRequest farmRequest, Member member) {

        String gatewaySerialId = farmRequest.serialId();

        Gateway gateway = gatewayRepository.findBySerialId(gatewaySerialId)
                .orElseThrow(() -> new RuntimeException("Gateway not found"));

        Farm farm = Farm.toEntity(farmRequest, gateway, member);
        farmRepository.save(farm);

        return farm;
    }

    public List<FarmListResponse> getFarmListResponseList(Member member) {
        return farmRepository.findAllByMember(member);
    }

    @Transactional
    public List<Farm> getFarmList(Member member) {
        return farmRepository.findByMember(member);
    }
}
