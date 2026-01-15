package subport.application.spendingrecord.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.MemberSubscriptionDetail;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.spendingrecord.port.in.CreateSpendingRecordUseCase;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;

@Service
@RequiredArgsConstructor
public class CreateSpendingRecordService implements CreateSpendingRecordUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final UpdateMemberSubscriptionPort updateMemberSubscriptionPort;
	private final SaveSpendingRecordPort saveSpendingRecordPort;

	@Transactional
	@Override
	public void create() {
		List<MemberSubscriptionDetail> memberSubscriptionDetails =
			loadMemberSubscriptionPort.loadDetailsByNextPaymentDate(LocalDate.now());

		List<SpendingRecord> spendingRecords = memberSubscriptionDetails.stream()
			.map(MemberSubscriptionDetail::toSpendingRecord)
			.toList();

		saveSpendingRecordPort.save(spendingRecords);

		List<MemberSubscription> memberSubscriptions = memberSubscriptionDetails.stream()
			.map(detail -> {
				MemberSubscription memberSubscription = detail.toMemberSubscription();
				memberSubscription.increaseNextPaymentDateByMonths(detail.planDurationMonths());
				return memberSubscription;
			})
			.toList();

		updateMemberSubscriptionPort.updateNextPaymentDate(memberSubscriptions);
	}
}
