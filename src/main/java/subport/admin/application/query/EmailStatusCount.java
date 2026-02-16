package subport.admin.application.query;

import subport.domain.emailnotification.SendingStatus;

public record EmailStatusCount(
	SendingStatus status,
	long count
) {
}
