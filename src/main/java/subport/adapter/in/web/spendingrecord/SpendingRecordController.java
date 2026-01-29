package subport.adapter.in.web.spendingrecord;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.spendingrecord.port.in.DeleteSpendingRecordUseCase;
import subport.application.spendingrecord.port.in.GetDailyCalendarUseCase;
import subport.application.spendingrecord.port.in.GetMonthlyCalendarUseCase;
import subport.application.spendingrecord.port.in.dto.GetDailyCalendarResponse;
import subport.application.spendingrecord.port.in.dto.GetMonthlyCalendarResponse;

@RestController
@RequestMapping("/api/spending-records")
@RequiredArgsConstructor
public class SpendingRecordController {

	private final GetMonthlyCalendarUseCase getMonthlyCalendarUseCase;
	private final GetDailyCalendarUseCase getDailyCalendarUseCase;
	private final DeleteSpendingRecordUseCase deleteSpendingRecordUseCase;

	@GetMapping("/summary")
	public ResponseEntity<GetMonthlyCalendarResponse> getMonthlyCalendar(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestParam YearMonth yearMonth
	) {
		return ResponseEntity.ok(getMonthlyCalendarUseCase.get(
			oAuth2User.getMemberId(), yearMonth));
	}

	@GetMapping
	public ResponseEntity<GetDailyCalendarResponse> getDailyCalendar(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestParam LocalDate date
	) {
		return ResponseEntity.ok(getDailyCalendarUseCase.get(
			oAuth2User.getMemberId(), date));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpendingRecord(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long spendingRecordId
	) {
		deleteSpendingRecordUseCase.delete(oAuth2User.getMemberId(), spendingRecordId);

		return ResponseEntity.noContent()
			.build();
	}
}
