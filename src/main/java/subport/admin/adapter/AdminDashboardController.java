package subport.admin.adapter;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.DashboardRecentMembersResponse;
import subport.admin.application.dto.DashboardSignupTrendsResponse;
import subport.admin.application.dto.DashboardStatsResponse;
import subport.admin.application.dto.DashboardTodayEmailNotificationsResponse;
import subport.admin.application.dto.DashboardTopCustomSubscriptionsResponse;
import subport.admin.application.dto.DashboardTopSubscriptionsResponse;
import subport.admin.application.service.AdminDashboardService;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

	private final AdminDashboardService dashboardService;

	@GetMapping("/stats")
	public ResponseEntity<DashboardStatsResponse> getStats() {
		return ResponseEntity.ok(
			dashboardService.getStats(LocalDate.now())
		);
	}

	@GetMapping("/signup-trend")
	public ResponseEntity<DashboardSignupTrendsResponse> getSignUpTrend() {
		return ResponseEntity.ok(
			dashboardService.getSignUpTrend(LocalDate.now())
		);
	}

	@GetMapping("/recent-members")
	public ResponseEntity<DashboardRecentMembersResponse> getRecentMembers() {
		return ResponseEntity.ok(
			dashboardService.getRecentMembers()
		);
	}

	@GetMapping("/top-subscriptions")
	public ResponseEntity<DashboardTopSubscriptionsResponse> getTopSubscriptions() {
		return ResponseEntity.ok(
			dashboardService.getTopSubscriptions()
		);
	}

	@GetMapping("/top-custom-subscriptions")
	public ResponseEntity<DashboardTopCustomSubscriptionsResponse> getTopCustomSubscriptions() {
		return ResponseEntity.ok(
			dashboardService.getTopCustomSubscriptions()
		);
	}

	@GetMapping("/today-email-notifications")
	public ResponseEntity<DashboardTodayEmailNotificationsResponse> getTodayEmailNotifications() {
		return ResponseEntity.ok(
			dashboardService.getTodayEmailNotifications(LocalDate.now())
		);
	}
}
