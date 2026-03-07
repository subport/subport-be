package subport.api.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subport.domain.membersubscription.MemberSubscription;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {

	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		JOIN FETCH ms.member
		JOIN FETCH ms.subscription
		JOIN FETCH ms.plan
		WHERE ms.id = :id
		""")
	Optional<MemberSubscription> findByIdWithFetch(Long id);

	List<MemberSubscription> findByMemberIdAndActiveIsTrue(Long memberId);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscription> findByMemberIdAndActive(
		Long memberId,
		boolean active,
		Sort sort
	);

	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		WHERE ms.member.id = :memberId
		AND ms.lastPaymentDate >= :start
		AND ms.lastPaymentDate < :end
		AND ms.active = true
		""")
	List<MemberSubscription> findByMemberIdAndLastPaymentDateBetween(
		@Param("memberId") Long memberId,
		@Param("start") LocalDate start,
		@Param("end") LocalDate end
	);

	List<MemberSubscription> findByMemberIdAndLastPaymentDateAndActiveTrue(Long memberId, LocalDate lastPaymentDate);

	void deleteByMemberId(Long memberId);
}
