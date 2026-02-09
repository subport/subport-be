package subport.application.member.port.in.dto;

public record GetReminderSettingsResponse(
	boolean paymentReminderEnabled,
	int reminderDaysBefore
) {
}
