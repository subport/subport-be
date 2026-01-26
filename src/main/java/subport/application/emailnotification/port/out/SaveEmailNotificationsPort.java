package subport.application.emailnotification.port.out;

import java.util.List;

import subport.domain.emailnotification.EmailNotification;

public interface SaveEmailNotificationsPort {

	void save(List<EmailNotification> emailNotifications);
}
