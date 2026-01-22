package subport.application.spendingrecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionForSpendingRecord;
import subport.application.spendingrecord.port.in.CreateSpendingRecordUseCase;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;

@Service
@RequiredArgsConstructor
public class CreateSpendingRecordService implements CreateSpendingRecordUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final UpdateMemberSubscriptionPort updateMemberSubscriptionPort;
	private final SaveSpendingRecordPort saveSpendingRecordPort;
	private final ExchangeRateService exchangeRateService;

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

				BigDecimal rate = memberSubscription.getExchangeRate();
				LocalDate exchangeRateDate = memberSubscription.getExchangeRateDate();
				if (rate != null && exchangeRateDate != null) {
					ExchangeRate exchangeRate =
						exchangeRateService.getExchangeRate(memberSubscription.getNextPaymentDate());

					rate = exchangeRate.getRate();
					exchangeRateDate = exchangeRate.getApplyDate();

					memberSubscription.updateExchangeRate(rate, exchangeRateDate);
				}

				memberSubscription.updateLastPaymentDate(detail.nextPaymentDate());
				memberSubscription.increaseNextPaymentDateByMonths(detail.planDurationMonths());

				return memberSubscription;
			})
			.toList();

		updateMemberSubscriptionPort.update(memberSubscriptions);
	}
}
