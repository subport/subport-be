package subport.application.emailnotification.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface LoadEmailNotificationPort {

	List<EmailNotification> loadEmailNotifications(LocalDate currentDate, SendingStatus status);
}
