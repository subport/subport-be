package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;

public record MemberSubscriptionSummary(
	Long id,
	String name,
	String logoImageUrl,
	BigDecimal amount,
	int period,
	long daysUntilPayment
) {

	public static MemberSubscriptionSummary from(
		MemberSubscriptionDetail detail,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
		return new MemberSubscriptionSummary(
			detail.id(),
			detail.subscriptionName(),
			detail.subscriptionLogoImageUrl(),
			actualPaymentAmount.setScale(0, RoundingMode.HALF_UP),
			detail.durationMonths(),
			daysUntilPayment
		);
	}
}
