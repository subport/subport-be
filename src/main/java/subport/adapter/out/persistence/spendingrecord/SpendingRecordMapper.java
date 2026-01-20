package subport.adapter.out.persistence.spendingrecord;

import org.springframework.stereotype.Component;

import subport.domain.spendingrecord.SpendingRecord;

@Component
public class SpendingRecordMapper {

	public SpendingRecordJpaEntity toJpaEntity(SpendingRecord spendingRecord) {
		return new SpendingRecordJpaEntity(
			spendingRecord.getPaymentDate(),
			spendingRecord.getAmount(),
			spendingRecord.getDurationMonths(),
			spendingRecord.getSubscriptionName(),
			spendingRecord.getSubscriptionLogoImageUrl(),
			spendingRecord.getMemberId()
		);
	}

	public SpendingRecord toDomain(SpendingRecordJpaEntity spendingRecordJpaEntity) {
		return new SpendingRecord(
			spendingRecordJpaEntity.getId(),
			spendingRecordJpaEntity.getPaymentDate(),
			spendingRecordJpaEntity.getAmount(),
			spendingRecordJpaEntity.getDurationMonths(),
			spendingRecordJpaEntity.getSubscriptionName(),
			spendingRecordJpaEntity.getSubscriptionLogoImageUrl(),
			spendingRecordJpaEntity.getMemberId()
		);
	}
}
