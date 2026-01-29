package subport.application.spendingrecord.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.spendingrecord.port.in.DeleteSpendingRecordUseCase;
import subport.application.spendingrecord.port.out.DeleteSpendingRecordPort;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.domain.spendingrecord.SpendingRecord;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteSpendingRecordService implements DeleteSpendingRecordUseCase {

	private final LoadSpendingRecordPort loadSpendingRecordPort;
	private final DeleteSpendingRecordPort deleteSpendingRecordPort;

	@Override
	public void delete(Long memberId, Long spendingRecordId) {
		SpendingRecord spendingRecord = loadSpendingRecordPort.loadSpendingRecord(spendingRecordId);

		if (!spendingRecord.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.SPENDING_RECORD_FORBIDDEN);
		}

		deleteSpendingRecordPort.delete(spendingRecord);
	}
}
