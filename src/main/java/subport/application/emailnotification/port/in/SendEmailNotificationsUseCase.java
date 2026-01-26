package subport.application.emailnotification.port.in;

import java.time.LocalDate;

public interface SendEmailNotificationsUseCase {

	void send(LocalDate currentDate);
}