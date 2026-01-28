package subport.application.spendingrecord.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

public record SpendingRecordInfo(
	Long id,
	String subscriptionName,
	String subscriptionLogoImageUrl,
	BigDecimal amount,
	int period,
	boolean inProgress
) {

	public static SpendingRecordInfo from(SpendingRecord spendingRecord) {
		return new SpendingRecordInfo(
			spendingRecord.getId(),
			spendingRecord.getSubscriptionName(),
			spendingRecord.getSubscriptionLogoImageUrl(),
			spendingRecord.getAmount().setScale(0, RoundingMode.HALF_UP),
			spendingRecord.getDurationMonths(),
			false
		);
	}

	public static SpendingRecordInfo from(MemberSubscription memberSubscription) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();

		return new SpendingRecordInfo(
			null,
			subscription.getName(),
			subscription.getLogoImageUrl(),
			memberSubscription.calculateActualPaymentAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getDurationMonths(),
			true
		);
	}
}
