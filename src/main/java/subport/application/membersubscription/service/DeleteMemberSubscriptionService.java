package subport.application.membersubscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.DeleteMemberSubscriptionUseCase;
import subport.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Service
@RequiredArgsConstructor
public class DeleteMemberSubscriptionService implements DeleteMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final DeleteMemberSubscriptionPort deleteMemberSubscriptionPort;

	@Transactional
	@Override
	public void delete(Long memberId, Long memberSubscriptionId) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		deleteMemberSubscriptionPort.delete(memberSubscription.getId());
	}
}
