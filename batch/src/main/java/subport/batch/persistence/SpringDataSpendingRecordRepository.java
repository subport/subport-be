package subport.batch.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.spendingrecord.SpendingRecord;

public interface SpringDataSpendingRecordRepository extends JpaRepository<SpendingRecord, Long> {
}
