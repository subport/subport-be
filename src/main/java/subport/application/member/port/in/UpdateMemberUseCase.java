package subport.application.member.port.in;

import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.GetReminderSettingsResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;
import subport.application.member.port.in.dto.UpdateReminderSettingsRequest;

public interface UpdateMemberUseCase {

	GetMemberResponse updateMember(Long memberId, UpdateMemberRequest request);

	GetReminderSettingsResponse updateReminderSettings(Long memberId, UpdateReminderSettingsRequest request);
}
