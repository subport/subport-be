package subport.admin.application.subscription.dto;

import java.time.LocalDate;

import subport.domain.subscription.Subscription;

public record SubscriptionResponse(
	Long id,
	String name,
	String type,
	String logoImageUrl,
	String planUrl,
	LocalDate createdAt,
	LocalDate lastModifiedAt
) {

	public static SubscriptionResponse from(Subscription subscription) {
		return new SubscriptionResponse(
			subscription.getId(),
			subscription.getName(),
			subscription.getType().getDisplayName(),
			subscription.getLogoImageUrl(),
			subscription.getPlanUrl(),
			subscription.getCreatedAt().toLocalDate(),
			subscription.getLastModifiedAt().toLocalDate()
		);
	}
}
