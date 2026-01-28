package subport.application.spendingrecord.port.in.dto;

import java.util.List;

public record GetDailyCalendarResponse(List<SpendingRecordInfo> spendingRecords) {
}
