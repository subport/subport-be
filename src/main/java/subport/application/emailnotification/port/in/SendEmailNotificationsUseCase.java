package subport.application.emailnotification.port.in;

import java.time.LocalDateTime;

public interface SendEmailNotificationsUseCase {

	void send(LocalDateTime currentDateTime);
}