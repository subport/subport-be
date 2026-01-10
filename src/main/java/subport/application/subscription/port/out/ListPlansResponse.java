package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.Plan;

public record ListPlansResponse(List<ReadPlanResponse> plans) {

	public static ListPlansResponse fromDomains(List<Plan> plans) {
		return new ListPlansResponse(
			plans.stream()
				.map(ReadPlanResponse::fromDomain)
				.toList()
		);
	}
}
