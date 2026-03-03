package subport.api.application.plan.port.in.dto;

import java.util.List;

import subport.domain.plan.Plan;

public record GetPlansResponse(
	String planUrl,
	List<GetPlanResponse> plans
) {

	public static GetPlansResponse of(String planUrl, List<Plan> plans) {
		return new GetPlansResponse(
			planUrl,
			plans.stream()
				.map(GetPlanResponse::from)
				.toList()
		);
	}
}
