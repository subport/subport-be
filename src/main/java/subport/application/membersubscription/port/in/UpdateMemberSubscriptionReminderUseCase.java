package subport.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionReminderRequest;

public interface UpdateMemberSubscriptionReminderUseCase {

	GetMemberSubscriptionResponse updateReminder(
		Long memberId,
		UpdateMemberSubscriptionReminderRequest request,
		Long memberSubscriptionId,
		LocalDate currentDate
	);
}
