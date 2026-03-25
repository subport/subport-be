package subport.admin.adapter.in.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.emailnotification.EmailNotificationQueryService;
import subport.admin.application.emailnotification.dto.EmailNotificationDetailResponse;
import subport.admin.application.emailnotification.dto.EmailNotificationsResponse;
import subport.domain.emailnotification.SendingStatus;

@RestController
@RequestMapping("/admin/email-notifications")
@RequiredArgsConstructor
public class EmailNotificationController {

	private final EmailNotificationQueryService emailNotificationQueryService;

	@GetMapping
	public ResponseEntity<EmailNotificationsResponse> searchEmailNotifications(
		@RequestParam(required = false) LocalDate date,
		@RequestParam(required = false) SendingStatus status,
		@RequestParam(required = false) Integer daysBeforePayment,
		@RequestParam(required = false) String email,
		@PageableDefault(size = 15) Pageable pageable
	) {
		return ResponseEntity.ok(
			emailNotificationQueryService.searchEmailNotifications(
				date,
				status,
				daysBeforePayment,
				email,
				pageable
			)
		);
	}

	@GetMapping("/details")
	public ResponseEntity<List<EmailNotificationDetailResponse>> getEmailNotificationDetails(
		@RequestParam String email,
		@RequestParam LocalDate paymentDate,
		@RequestParam Integer daysBeforePayment
	) {
		return ResponseEntity.ok(
			emailNotificationQueryService.getEmailNotificationDetails(email, paymentDate, daysBeforePayment)
		);
	}
}
