package subport.application.member.port.in;

import java.time.LocalDate;

import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.in.dto.GetMemberReminderSettingsResponse;
import subport.application.member.port.in.dto.GetMemberResponse;

public interface MemberQueryUseCase {

	GetMemberResponse getMember(Long memberId);

	GetMemberProfileResponse getMemberProfile(Long memberId, LocalDate currentDate);

	GetMemberReminderSettingsResponse getMemberReminderSettings(Long memberId);
}
