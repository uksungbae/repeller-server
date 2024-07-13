package school.kku.repellingserver.farm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import school.kku.repellingserver.farm.domain.Farm;
import school.kku.repellingserver.farm.dto.FarmListResponse;
import school.kku.repellingserver.member.domain.Member;

import java.util.List;

import static school.kku.repellingserver.farm.domain.QFarm.farm;
import static school.kku.repellingserver.repellent.repellentDevice.domain.QRepellentDevice.repellentDevice;

@RequiredArgsConstructor
public class FarmRepositoryImpl implements FarmRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FarmListResponse> findAllByMember(Member member) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                FarmListResponse.class,
                                farm.id,
                                farm.name,
                                JPAExpressions.select(repellentDevice.count())
                                        .from(repellentDevice)
                                        .where(
                                                repellentDevice.farm.eq(farm),
                                                repellentDevice.isActivated.eq(true)
                                                )
                                , farm.address
                        )
                ).from(farm)
                .where(farm.member.eq(member))
                .fetch();
    }

    @Override
    public List<Farm> findByMember(Member member) {
        return jpaQueryFactory.selectFrom(farm)
                .where(farm.member.eq(member))
                .fetch();
    }
}
