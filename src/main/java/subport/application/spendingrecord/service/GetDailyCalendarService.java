package subport.application.spendingrecord.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.spendingrecord.port.in.GetDailyCalendarUseCase;
import subport.application.spendingrecord.port.in.dto.GetDailyCalendarResponse;
import subport.application.spendingrecord.port.in.dto.SpendingRecordInfo;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetDailyCalendarService implements GetDailyCalendarUseCase {

	private final LoadSpendingRecordPort loadSpendingRecordPort;
	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;

	@Override
	public GetDailyCalendarResponse get(Long memberId, LocalDate targetDate) {
		List<SpendingRecordInfo> spendingRecords = Stream.concat(
				loadSpendingRecordPort.loadSpendingRecords(memberId, targetDate).stream()
					.map(SpendingRecordInfo::from),
				loadMemberSubscriptionPort.loadMemberSubscriptions(memberId, targetDate).stream()
					.map(SpendingRecordInfo::from)
			)
			.sorted(Comparator.comparing(SpendingRecordInfo::subscriptionName))
			.toList();

		return new GetDailyCalendarResponse(spendingRecords);
	}
}
