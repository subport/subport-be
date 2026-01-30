package subport.application.spendingrecord.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.spendingrecord.port.in.CreateSpendingRecordUseCase;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSpendingRecordService implements CreateSpendingRecordUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final SaveSpendingRecordPort saveSpendingRecordPort;
	private final ExchangeRateService exchangeRateService;

	@Override
	public void create(LocalDateTime currentDateTime) {
		List<MemberSubscription> memberSubscriptions =
			loadMemberSubscriptionPort.loadMemberSubscriptions(currentDateTime.toLocalDate());

		List<SpendingRecord> spendingRecords = memberSubscriptions.stream()
			.map(memberSubscription -> {
				Subscription subscription = memberSubscription.getSubscription();
				Plan plan = memberSubscription.getPlan();

				return new SpendingRecord(
					memberSubscription.getLastPaymentDate(),
					memberSubscription.calculateActualPaymentAmount(),
					plan.getDurationMonths(),
					subscription.getName(),
					subscription.getLogoImageUrl(),
					subscription.getMember().getId(),
					memberSubscription.getId()
				);
			})
			.toList();
		saveSpendingRecordPort.save(spendingRecords);

		for (MemberSubscription memberSubscription : memberSubscriptions) {
			BigDecimal rate = memberSubscription.getExchangeRate();
			LocalDate exchangeRateDate = memberSubscription.getExchangeRateDate();

			if (rate != null && exchangeRateDate != null) {
				ExchangeRate exchangeRate =
					exchangeRateService.getExchangeRate(memberSubscription.getNextPaymentDate(), currentDateTime);

				rate = exchangeRate.getRate();
				exchangeRateDate = exchangeRate.getApplyDate();

				memberSubscription.updateExchangeRate(rate, exchangeRateDate);
			}

			memberSubscription.updateLastPaymentDate();
		}
	}
}
