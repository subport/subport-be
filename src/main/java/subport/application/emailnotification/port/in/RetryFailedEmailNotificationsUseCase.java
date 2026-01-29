package subport.application.emailnotification.port.in;

import java.time.LocalDateTime;

public interface RetryFailedEmailNotificationsUseCase {

	void retry(LocalDateTime currentDateTime);
}
