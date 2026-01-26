package subport.application.emailnotification.port.in;

import java.time.LocalDate;

public interface RetryFailedEmailNotificationsUseCase {

	void retry(LocalDate currentDate);
}
