package subport.api.application.spendingrecord.port.in;

public interface DeleteSpendingRecordUseCase {

	void delete(Long memberId, Long spendingRecordId);
}
