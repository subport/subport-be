package subport.application.membersubscription.port.in;

public interface DeactivateMemberSubscriptionUseCase {

	void deactivate(Long memberId, Long memberSubscriptionId);
}
