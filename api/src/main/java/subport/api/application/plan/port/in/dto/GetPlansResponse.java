package subport.api.application.plan.port.in.dto;

import java.util.List;

import subport.domain.plan.Plan;

public record GetPlansResponse(List<GetPlanResponse> plans) {

	public static GetPlansResponse from(List<Plan> plans) {
		return new GetPlansResponse(
			plans.stream()
				.map(GetPlanResponse::from)
				.toList()
		);
	}
}
