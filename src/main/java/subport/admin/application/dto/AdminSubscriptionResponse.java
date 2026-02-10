package subport.admin.application.dto;

import java.time.LocalDate;

import subport.domain.subscription.Subscription;

public record AdminSubscriptionResponse(
	Long id,
	String name,
	String type,
	String logoImageUrl,
	String planUrl,
	LocalDate createdAt,
	LocalDate lastModifiedAt
) {

	public static AdminSubscriptionResponse from(Subscription subscription) {
		return new AdminSubscriptionResponse(
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
