package subport.application.spendingrecord.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.spendingrecord.port.in.CreateSpendingRecordsUseCase;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSpendingRecordsService implements CreateSpendingRecordsUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final SaveSpendingRecordPort saveSpendingRecordPort;
	private final ExchangeRateService exchangeRateService;
	private final LoadSpendingRecordPort loadSpendingRecordPort;

	@Override
	public void createForScheduling(LocalDateTime currentDateTime) {
		List<MemberSubscription> memberSubscriptions =
			loadMemberSubscriptionPort.loadMemberSubscriptions(currentDateTime.toLocalDate());

		List<SpendingRecord> spendingRecords = memberSubscriptions.stream()
			.map(memberSubscription -> {
				SpendingRecord spendingRecord = createSpendingRecord(memberSubscription);
				updateMemberSubscription(memberSubscription, currentDateTime);

				return spendingRecord;
			})
			.toList();
		saveSpendingRecordPort.save(spendingRecords);
	}

	@Override
	public void createMissing(MemberSubscription memberSubscription, LocalDateTime currentDateTime) {
		List<SpendingRecord> spendingRecords = new ArrayList<>();
		LocalDate nextPaymentDate = memberSubscription.getNextPaymentDate();
		while (!nextPaymentDate.isAfter(currentDateTime.toLocalDate())) {
			SpendingRecord spendingRecord;
			if (nextPaymentDate.isEqual(currentDateTime.toLocalDate())
				&& !loadSpendingRecordPort.existsSpendingRecord(nextPaymentDate, memberSubscription.getId())) {
				spendingRecord = createSpendingRecord(memberSubscription);
				spendingRecords.add(spendingRecord);
				updateMemberSubscription(memberSubscription, currentDateTime);
				break;
			}

			spendingRecord = createSpendingRecord(memberSubscription);
			spendingRecords.add(spendingRecord);
			updateMemberSubscription(memberSubscription, currentDateTime);

			nextPaymentDate = memberSubscription.getNextPaymentDate();
		}

		if (!spendingRecords.isEmpty()) {
			saveSpendingRecordPort.save(spendingRecords);
		}
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

	private void updateMemberSubscription(MemberSubscription memberSubscription, LocalDateTime currentDateTime) {
		memberSubscription.updateLastPaymentDate();

		if (memberSubscription.isExchangeRateApplicable()) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(
				memberSubscription.getLastPaymentDate(),
				currentDateTime
			);

			memberSubscription.updateExchangeRate(
				exchangeRate.getRate(),
				exchangeRate.getApplyDate()
			);
		}
	}
}
