package subport.application.membersubscription.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.DeactivateMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Service
@Transactional
@RequiredArgsConstructor
public class DeactivateMemberSubscriptionService implements DeactivateMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final MemberSubscriptionQueryUseCase memberSubscriptionQueryUseCase;

	@Override
	public GetMemberSubscriptionResponse deactivate(
		Long memberId,
		Long memberSubscriptionId,
		LocalDate currentDate
	) {
		MemberSubscription memberSubscription =
			loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}
		if (!memberSubscription.isActive()) {
			return memberSubscriptionQueryUseCase.getMemberSubscription(
				memberId,
				memberSubscriptionId,
				currentDate
			);
		}

		memberSubscription.deactivate();

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDate
		);
	}
}
