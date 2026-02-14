package subport.admin.adapter;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.DashboardRecentMembersResponse;
import subport.admin.application.dto.DashboardSignupTrend;
import subport.admin.application.dto.DashboardStatsResponse;
import subport.admin.application.dto.DashboardTopServicesResponse;
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
	public ResponseEntity<DashboardSignupTrend> getSignUpTrend() {
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

	@GetMapping("/top-services")
	public ResponseEntity<DashboardTopServicesResponse> getTopServices() {
		return ResponseEntity.ok(
			dashboardService.getTopServices()
		);
	}
}
