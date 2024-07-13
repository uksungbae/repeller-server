package school.kku.repellingserver.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kku.repellingserver.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNameAndEmail(String name, String email);

}
