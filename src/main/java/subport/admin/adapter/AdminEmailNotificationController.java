package subport.admin.adapter;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminEmailNotificationsResponse;
import subport.admin.application.service.AdminEmailNotificationService;
import subport.domain.emailnotification.SendingStatus;

@RestController
@RequestMapping("/admin/email-notifications")
@RequiredArgsConstructor
public class AdminEmailNotificationController {

	private final AdminEmailNotificationService emailNotificationService;

	@GetMapping
	public ResponseEntity<AdminEmailNotificationsResponse> searchEmailNotifications(
		@RequestParam(required = false) LocalDate date,
		@RequestParam(required = false) SendingStatus status,
		@RequestParam(required = false) Integer daysBeforePayment,
		@RequestParam(required = false) String email,
		@PageableDefault(
			page = 0,
			size = 15,
			sort = "paymentDate",
			direction = Sort.Direction.DESC
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			emailNotificationService.searchEmailNotifications(
				date,
				status,
				daysBeforePayment,
				email,
				pageable
			)
		);
	}
}
