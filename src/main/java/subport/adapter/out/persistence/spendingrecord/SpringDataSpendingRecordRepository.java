package subport.adapter.out.persistence.spendingrecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSpendingRecordRepository extends JpaRepository<SpendingRecordJpaEntity, Long> {

	List<SpendingRecordJpaEntity> findTop3ByMemberIdAndSubscriptionNameOrderByPaymentDateDesc(
		Long memberId,
		String subscriptionName
	);
}
