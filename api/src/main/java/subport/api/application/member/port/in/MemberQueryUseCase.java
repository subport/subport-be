package subport.api.application.member.port.in;

import java.time.LocalDate;

import subport.api.application.member.port.in.dto.GetMemberProfileResponse;
import subport.api.application.member.port.in.dto.GetMemberResponse;
import subport.api.application.member.port.in.dto.GetReminderSettingsResponse;

public interface MemberQueryUseCase {

	GetMemberResponse getMember(Long memberId);

	GetMemberProfileResponse getMemberProfile(Long memberId, LocalDate currentDate);

	GetReminderSettingsResponse getMemberReminderSettings(Long memberId);
}
