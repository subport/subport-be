package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.SubscriptionPlan;

public record ListSubscriptionPlansResponse(List<ReadSubscriptionPlanResponse> plans) {

	public static ListSubscriptionPlansResponse fromDomains(List<SubscriptionPlan> plans) {
		return new ListSubscriptionPlansResponse(
			plans.stream()
				.map(ReadSubscriptionPlanResponse::fromDomain)
				.toList()
		);
	}
}
