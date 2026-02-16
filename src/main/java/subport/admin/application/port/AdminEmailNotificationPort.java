package subport.admin.application.port;

import java.time.LocalDate;
import java.util.List;

import subport.admin.application.query.EmailStatusCount;
import subport.domain.emailnotification.EmailNotification;

public interface AdminEmailNotificationPort {

	List<EmailNotification> loadEmailNotifications(LocalDate date);

	List<EmailStatusCount> countTodayByStatus(LocalDate date);
}
