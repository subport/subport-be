package subport.api.application.spendingrecord.port.in;

import java.time.LocalDate;

import subport.api.application.spendingrecord.port.in.dto.GetDailyCalendarResponse;

public interface GetDailyCalendarUseCase {

	GetDailyCalendarResponse get(Long memberId, LocalDate targetDate);
}
