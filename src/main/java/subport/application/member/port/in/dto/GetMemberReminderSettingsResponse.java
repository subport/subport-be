package subport.application.member.port.in.dto;

public record GetMemberReminderSettingsResponse(
	boolean paymentReminderEnabled,
	int reminderDaysBefore
) {
}
