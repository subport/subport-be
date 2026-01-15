package subport.adapter.out.persistence.spendingrecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSpendingRecordRepository extends JpaRepository<SpendingRecordJpaEntity, Long> {
}
