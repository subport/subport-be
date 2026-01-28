package subport.adapter.out.persistence.spendingrecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.spendingrecord.SpendingRecord;

public interface SpringDataSpendingRecordRepository extends JpaRepository<SpendingRecord, Long> {

	List<SpendingRecord> findTop3ByMemberSubscriptionIdOrderByPaymentDateDesc(Long memberSubscriptionId);
}
