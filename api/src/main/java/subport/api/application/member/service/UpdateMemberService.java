package subport.api.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.member.port.in.MemberQueryUseCase;
import subport.api.application.member.port.in.UpdateMemberUseCase;
import subport.api.application.member.port.in.dto.GetMemberResponse;
import subport.api.application.member.port.in.dto.GetReminderSettingsResponse;
import subport.api.application.member.port.in.dto.UpdateMemberRequest;
import subport.api.application.member.port.in.dto.UpdateReminderSettingsRequest;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.domain.member.Member;
import subport.domain.membersubscription.MemberSubscription;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService implements UpdateMemberUseCase {

	private final LoadMemberPort loadMemberPort;
	private final MemberQueryUseCase memberQueryUseCase;
	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;

	@Override
	public GetMemberResponse updateMember(Long memberId, UpdateMemberRequest request) {
		Member member = loadMemberPort.load(memberId);

		member.update(
			request.nickname(),
			request.email()
		);

		return memberQueryUseCase.getMember(memberId);
	}

	@Override
	public GetReminderSettingsResponse updateReminderSettings(Long memberId, UpdateReminderSettingsRequest request) {
		Member member = loadMemberPort.load(memberId);

		boolean isReminderEnabled = request.paymentReminderEnabled();
		int reminderDaysBefore = request.reminderDaysBefore();

		member.updateReminderSettings(
			isReminderEnabled,
			reminderDaysBefore
		);

		loadMemberSubscriptionPort.loadMemberSubscriptions(memberId)
			.forEach(MemberSubscription::updatePaymentReminderDate);

		return new GetReminderSettingsResponse(
			member.isPaymentReminderEnabled(),
			member.getReminderDaysBefore()
		);
	}
}
