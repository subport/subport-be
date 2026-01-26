package subport.application.emailnotification.port.in;

import java.time.LocalDate;

public interface CreateEmailNotificationUseCase {

	void create(LocalDate currentDate);
}
