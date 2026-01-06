package subport.adapter.out.persistence.membersubscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscriptionJpaEntity, Long> {
}
