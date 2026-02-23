package subport.admin.application.dashboard.dto;

import java.time.LocalDate;

public record DashboardSignupTrendResponse(
	LocalDate date,
	long newMemberCount
) {
}
