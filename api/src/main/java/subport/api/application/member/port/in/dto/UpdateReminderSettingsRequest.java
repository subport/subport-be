package subport.api.application.member.port.in.dto;

public record UpdateReminderSettingsRequest(
	boolean paymentReminderEnabled,
	int reminderDaysBefore
) {
}
