package subport.api.application.membersubscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.membersubscription.port.in.DeleteMemberSubscriptionUseCase;
import subport.api.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.membersubscription.MemberSubscription;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteMemberSubscriptionService implements DeleteMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final DeleteMemberSubscriptionPort deleteMemberSubscriptionPort;

	@Override
	public void delete(Long memberId, Long memberSubscriptionId) {
		MemberSubscription memberSubscription =
			loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		deleteMemberSubscriptionPort.delete(memberSubscription);
	}
}
