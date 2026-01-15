package subport.domain.spendingrecord;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Getter
public class SpendingRecord {

	private final Long id;

	private final LocalDate paymentDate;

	private final BigDecimal amount;

	private final int durationMonths;

	private final String subscriptionName;

	private final String subscriptionLogoImageUrl;

	private final Long memberId;

	public SpendingRecord(
		Long id,
		LocalDate paymentDate,
		BigDecimal amount,
		int durationMonths,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		Long memberId
	) {
		this.id = id;
		this.paymentDate = paymentDate;
		this.amount = amount;
		this.durationMonths = durationMonths;
		this.subscriptionName = subscriptionName;
		this.subscriptionLogoImageUrl = subscriptionLogoImageUrl;
		this.memberId = memberId;
	}

	public static SpendingRecord withId(
		Long id,
		LocalDate paymentDate,
		BigDecimal amount,
		int durationMonths,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		Long memberId
	) {
		return new SpendingRecord(
			id,
			paymentDate,
			amount,
			durationMonths,
			subscriptionName,
			subscriptionLogoImageUrl,
			memberId
		);
	}

	public static SpendingRecord withoutId(
		LocalDate paymentDate,
		BigDecimal amount,
		int durationMonths,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		Long memberId
	) {
		return new SpendingRecord(
			null,
			paymentDate,
			amount,
			durationMonths,
			subscriptionName,
			subscriptionLogoImageUrl,
			memberId
		);
	}

	public static SpendingRecord from(
		Plan plan,
		Subscription subscription,
		MemberSubscription memberSubscription
	) {
		BigDecimal amount = plan.getAmount();
		BigDecimal dutchPayAmount = memberSubscription.getDutchPayAmount();
		if (memberSubscription.isDutchPay() && dutchPayAmount != null) {
			amount = dutchPayAmount;
		}
		return new SpendingRecord(
			null,
			memberSubscription.getNextPaymentDate(),
			amount,
			plan.getDurationMonths(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			memberSubscription.getMemberId()
		);
	}
}
