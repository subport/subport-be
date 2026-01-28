package subport.application.spendingrecord.port.in;

import java.time.YearMonth;

import subport.application.spendingrecord.port.in.dto.GetMonthlyCalendarResponse;

public interface GetMonthlyCalendarUseCase {

	GetMonthlyCalendarResponse get(Long memberId, YearMonth targetYearMonth);
}
