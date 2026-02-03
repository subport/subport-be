package subport.application.spendingrecord.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

public record SpendingRecordInfo(
	Long spendingRecordId,
	Long memberSubscriptionId,
	String subscriptionName,
	String subscriptionLogoImageUrl,
	BigDecimal amount,
	int period
) {

	public static SpendingRecordInfo from(SpendingRecord spendingRecord) {
		return new SpendingRecordInfo(
			spendingRecord.getId(),
			null,
			spendingRecord.getSubscriptionName(),
			spendingRecord.getSubscriptionLogoImageUrl(),
			spendingRecord.getAmount().setScale(0, RoundingMode.HALF_UP),
			spendingRecord.getDurationMonths()
		);
	}

	public static SpendingRecordInfo from(MemberSubscription memberSubscription) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();

		return new SpendingRecordInfo(
			null,
			memberSubscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			memberSubscription.calculateActualPaymentAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getDurationMonths()
		);
	}
}
