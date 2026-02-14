package subport.admin.application.dto;

import java.time.LocalDate;

public record DashboardSignupTrendResponse(
	LocalDate date,
	long newMemberCount
) {
}
