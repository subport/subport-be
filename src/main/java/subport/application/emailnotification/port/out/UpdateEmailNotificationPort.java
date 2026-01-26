package subport.application.emailnotification.port.out;

import subport.domain.emailnotification.EmailNotification;

public interface UpdateEmailNotificationPort {

	void update(EmailNotification emailNotification);
}
