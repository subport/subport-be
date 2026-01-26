package subport.application.emailnotification.port.in;

import java.time.LocalDate;

public interface CreateEmailNotificationsUseCase {

	void create(LocalDate currentDate);
}
