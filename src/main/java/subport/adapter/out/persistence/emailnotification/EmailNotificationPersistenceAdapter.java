package subport.adapter.out.persistence.emailnotification;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;
	private final EmailNotificationMapper emailNotificationMapper;
}
