package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.ListPlansResponse;
import subport.application.subscription.port.in.dto.ReadPlanResponse;

public interface ReadPlanUseCase {

	ReadPlanResponse read(Long memberId, Long planId);

	ListPlansResponse list(Long memberId, Long subscriptionId);
}
