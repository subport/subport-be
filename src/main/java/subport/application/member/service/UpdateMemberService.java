package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.MemberQueryUseCase;
import subport.application.member.port.in.UpdateMemberUseCase;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.GetReminderSettingsResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;
import subport.application.member.port.in.dto.UpdateReminderSettingsRequest;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService implements UpdateMemberUseCase {

	private final LoadMemberPort loadMemberPort;
	private final MemberQueryUseCase memberQueryUseCase;

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

		member.updateReminderSettings(
			request.paymentReminderEnabled(),
			request.reminderDaysBefore()
		);

		return new GetReminderSettingsResponse(
			member.isPaymentReminderEnabled(),
			member.getReminderDaysBefore()
		);
	}
}
