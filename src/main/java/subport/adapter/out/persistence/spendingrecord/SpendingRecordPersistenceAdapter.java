package subport.adapter.out.persistence.spendingrecord;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.spendingrecord.SpendingRecord;

@Component
@RequiredArgsConstructor
public class SpendingRecordPersistenceAdapter implements
	SaveSpendingRecordPort,
	LoadSpendingRecordPort {

	private final SpringDataSpendingRecordRepository spendingRecordRepository;

	@Override
	public void save(List<SpendingRecord> spendingRecords) {
		spendingRecordRepository.saveAll(spendingRecords);
	}

	@Override
	public List<SpendingRecord> loadSpendingRecords(Long memberSubscriptionId) {
		return spendingRecordRepository.findTop3ByMemberSubscriptionIdOrderByPaymentDateDesc(memberSubscriptionId);
	}

	@Override
	public List<SpendingRecord> loadSpendingRecords(Long memberId, LocalDate start, LocalDate end) {
		return spendingRecordRepository.findByMemberIdAndPaymentDateGreaterThanEqualAndPaymentDateLessThan(
			memberId,
			start,
			end
		);
	}

	@Override
	public List<SpendingRecord> loadSpendingRecords(Long memberId, LocalDate targetDate) {
		return spendingRecordRepository.findByMemberIdAndPaymentDate(memberId, targetDate);
	}
}
