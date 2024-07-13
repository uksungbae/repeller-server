package school.kku.repellingserver.farm.repository;

import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.dto.FarmListResponse;
import school.kku.repellingserver.member.domain.Member;

import java.util.List;

public interface FarmRepositoryCustom {

    List<FarmListResponse> findAllByMember(Member member);

    List<Farm> findByMember(Member member);

}
