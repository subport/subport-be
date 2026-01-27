package subport.adapter.out.persistence.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.member.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByProviderId(String providerId);
}
