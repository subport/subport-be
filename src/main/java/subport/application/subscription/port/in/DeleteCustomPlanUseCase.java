package subport.application.subscription.port.in;

public interface DeleteCustomPlanUseCase {

	void delete(Long memberId, Long planId);
}
