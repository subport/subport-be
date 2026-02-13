package subport.admin.adapter;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.DashboardStatsResponse;
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
}
