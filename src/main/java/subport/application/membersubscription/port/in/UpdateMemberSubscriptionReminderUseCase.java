package subport.application.membersubscription.port.in;

public interface UpdateMemberSubscriptionReminderUseCase {

	void updateReminder(
		Long memberId,
		UpdateMemberSubscriptionReminderRequest request,
		Long memberSubscriptionId
	);
}
