package subport.application.spendingrecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.membersubscription.port.out.LoadExchangeRatePort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionForSpendingRecord;
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
	private final LoadExchangeRatePort loadExchangeRatePort;

	@Transactional
	@Override
	public void create() {
		List<MemberSubscriptionForSpendingRecord> memberSubscriptionsForSpendingRecord =
			loadMemberSubscriptionPort.loadForSpendingRecordByNextPaymentDate(LocalDate.now());

		List<SpendingRecord> spendingRecords = memberSubscriptionsForSpendingRecord.stream()
			.map(MemberSubscriptionForSpendingRecord::toSpendingRecord)
			.toList();

		saveSpendingRecordPort.save(spendingRecords);

		List<MemberSubscription> memberSubscriptions = memberSubscriptionsForSpendingRecord.stream()
			.map(detail -> {
				MemberSubscription memberSubscription = detail.toMemberSubscription();

				BigDecimal exchangeRate = memberSubscription.getExchangeRate();
				LocalDate exchangeRateDate = memberSubscription.getExchangeRateDate();
				if (exchangeRate != null && exchangeRateDate != null) {
					LocalDate nextPaymentDate = memberSubscription.getNextPaymentDate();
					exchangeRate = loadExchangeRatePort.load(nextPaymentDate.toString());
					memberSubscription.updateExchangeRate(exchangeRate, nextPaymentDate);
				}

				memberSubscription.increaseNextPaymentDateByMonths(detail.planDurationMonths());

				return memberSubscription;
			})
			.toList();

		updateMemberSubscriptionPort.update(memberSubscriptions);
	}
}
