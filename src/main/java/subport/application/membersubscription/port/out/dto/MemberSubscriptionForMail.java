package subport.application.membersubscription.port.out.dto;

import java.time.LocalDate;

public record MemberSubscriptionForMail(
	Long memberSubscriptionId,
	LocalDate paymentDate,
	Integer daysBeforePayment,
	Long memberId,
	String memberEmail,
	String subscriptionName,
	String subscriptionLogoImageUrl
) {
}
