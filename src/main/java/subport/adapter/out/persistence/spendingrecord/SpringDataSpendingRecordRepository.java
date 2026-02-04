package subport.adapter.out.persistence.spendingrecord;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.spendingrecord.SpendingRecord;

public interface SpringDataSpendingRecordRepository extends JpaRepository<SpendingRecord, Long> {

	boolean existsByPaymentDateAndMemberSubscriptionId(LocalDate paymentDate, Long memberSubscriptionId);

	List<SpendingRecord> findTop3ByMemberSubscriptionIdOrderByPaymentDateDesc(Long memberSubscriptionId);

	List<SpendingRecord> findByMemberIdAndPaymentDateGreaterThanEqualAndPaymentDateLessThan(
		Long memberId,
		LocalDate paymentDateIsGreaterThan,
		LocalDate paymentDateIsLessThan
	);

	List<SpendingRecord> findByMemberIdAndPaymentDate(Long memberId, LocalDate paymentDate);
}
