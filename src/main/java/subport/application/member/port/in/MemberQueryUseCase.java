package subport.application.member.port.in;

import java.time.LocalDate;

import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.GetReminderSettingsResponse;

public interface MemberQueryUseCase {

	GetMemberResponse getMember(Long memberId);

	GetMemberProfileResponse getMemberProfile(Long memberId, LocalDate currentDate);

	GetReminderSettingsResponse getMemberReminderSettings(Long memberId);
}
