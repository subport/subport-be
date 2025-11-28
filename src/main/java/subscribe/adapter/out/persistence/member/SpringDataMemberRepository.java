package subscribe.adapter.out.persistence.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberRepository extends JpaRepository<MemberJpaEntity, Long> {

	Optional<MemberJpaEntity> findByProviderId(String providerId);
}
