package subport.adapter.out.persistence.spendingrecord;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.spendingrecord.SpendingRecord;

@Component
@RequiredArgsConstructor
public class SpendingRecordPersistenceAdapter implements SaveSpendingRecordPort {

	private final SpringDataSpendingRecordRepository spendingRecordRepository;
	private final SpendingRecordMapper spendingRecordMapper;

	@Override
	public void save(List<SpendingRecord> spendingRecords) {
		List<SpendingRecordJpaEntity> spendingRecordEntities = spendingRecords.stream()
			.map(spendingRecordMapper::toJpaEntity)
			.toList();

		spendingRecordRepository.saveAll(spendingRecordEntities);
	}
}
