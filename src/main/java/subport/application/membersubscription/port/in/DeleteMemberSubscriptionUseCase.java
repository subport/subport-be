package subport.application.membersubscription.port.in;

public interface DeleteMemberSubscriptionUseCase {

	void delete(Long memberId, Long memberSubscriptionId);
}
