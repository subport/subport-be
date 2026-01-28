package subport.adapter.in.web.spendingrecord;

import java.time.YearMonth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.spendingrecord.port.in.GetMonthlyCalendarUseCase;
import subport.application.spendingrecord.port.in.dto.GetMonthlyCalendarResponse;

@RestController
@RequestMapping("/api/spending-records")
@RequiredArgsConstructor
public class SpendingRecordController {

	private final GetMonthlyCalendarUseCase getMonthlyCalendarUseCase;

	@GetMapping("/summary")
	public ResponseEntity<GetMonthlyCalendarResponse> getMonthlyCalendar(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestParam YearMonth date
	) {
		return ResponseEntity.ok(getMonthlyCalendarUseCase.get(
			oAuth2User.getMemberId(), date));
	}
}
