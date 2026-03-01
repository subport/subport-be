package subport.batch.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.batch.persistence.SpringDataMemberSubscriptionRepository;
import subport.batch.persistence.SpringDataSpendingRecordRepository;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.plan.Plan;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class SpendingRecordService {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;
	private final SpringDataSpendingRecordRepository spendingRecordRepository;
	private final ExchangeRateService exchangeRateService;

	public void createForScheduling(LocalDateTime now) {
		List<MemberSubscription> memberSubscriptions =
			memberSubscriptionRepository.findActiveByNextPaymentDate(now.toLocalDate());

		List<SpendingRecord> spendingRecords = memberSubscriptions.stream()
			.map(memberSubscription -> {
				SpendingRecord spendingRecord = createSpendingRecord(memberSubscription);
				updateMemberSubscription(memberSubscription, now);

				return spendingRecord;
			})
			.toList();
		spendingRecordRepository.saveAll(spendingRecords);
	}

	private SpendingRecord createSpendingRecord(MemberSubscription memberSubscription) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();

		return new SpendingRecord(
			memberSubscription.getLastPaymentDate(),
			memberSubscription.calculateActualPaymentAmount(),
			plan.getDurationMonths(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			memberSubscription.getMember().getId(),
			memberSubscription.getId()
		);
	}

	private void updateMemberSubscription(MemberSubscription memberSubscription, LocalDateTime now) {
		memberSubscription.updateLastPaymentDate();

		if (memberSubscription.isExchangeRateApplicable()) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(
				memberSubscription.getLastPaymentDate(),
				now
			);

			memberSubscription.updateExchangeRate(
				exchangeRate.getRate(),
				exchangeRate.getApplyDate()
			);
		}
	}
}
