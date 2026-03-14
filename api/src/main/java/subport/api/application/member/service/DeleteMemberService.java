package subport.api.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.out.DeleteRefreshTokenPort;
import subport.api.application.member.port.in.DeleteMemberUseCase;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.member.port.out.UnlinkMemberPort;
import subport.api.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.api.application.plan.port.out.DeletePlanPort;
import subport.api.application.spendingrecord.port.out.DeleteSpendingRecordPort;
import subport.api.application.subscription.port.out.DeleteSubscriptionPort;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUseCase {

	private final LoadMemberPort loadMemberPort;
	private final UnlinkMemberPort unlinkMemberPort;
	private final DeleteMemberSubscriptionPort deleteMemberSubscriptionPort;
	private final DeletePlanPort deletePlanPort;
	private final DeleteSubscriptionPort deleteSubscriptionPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final DeleteSpendingRecordPort deleteSpendingRecordPort;

	@Override
	public void deleteMember(Long memberId) {
		Member member = loadMemberPort.load(memberId);

		unlinkMemberPort.unlink(member.getProviderId());

		deleteMemberSubscriptionPort.delete(memberId);
		deletePlanPort.deleteByMemberId(memberId);
		deleteSubscriptionPort.deleteByMemberId(memberId);
		deleteRefreshTokenPort.deleteByMemberId(memberId);
		deleteSpendingRecordPort.delete(memberId);

		member.withdraw();
	}
}
