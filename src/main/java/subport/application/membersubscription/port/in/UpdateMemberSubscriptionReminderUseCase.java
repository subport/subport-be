package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionReminderRequest;

public interface UpdateMemberSubscriptionReminderUseCase {

	void updateReminder(
		Long memberId,
		UpdateMemberSubscriptionReminderRequest request,
		Long memberSubscriptionId
	);
}
