package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.ReadMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.ReadMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.SpendingRecordSummary;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.domain.subscription.SubscriptionAmountUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadMemberSubscriptionService implements ReadMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadSpendingRecordPort loadSpendingRecordPort;

	@Override
	public ReadMemberSubscriptionResponse read(Long memberId, Long memberSubscriptionId) {
		MemberSubscriptionDetail memberSubscriptionDetail = loadMemberSubscriptionPort.loadDetail(memberSubscriptionId);

		if (!memberSubscriptionDetail.memberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		LocalDate nextPaymentDate = memberSubscriptionDetail.nextPaymentDate();
		LocalDate currentPaymentDate = nextPaymentDate.minusMonths(memberSubscriptionDetail.durationMonths());

		LocalDate now = LocalDate.now();
		long elapsedDays = ChronoUnit.DAYS.between(currentPaymentDate, now);
		long totalDays = ChronoUnit.DAYS.between(currentPaymentDate, nextPaymentDate);
		int paymentProgressPercent = (int)((double)elapsedDays / totalDays * 100);

		boolean dutchPay = memberSubscriptionDetail.dutchPay();
		SubscriptionAmountUnit amountUnit = memberSubscriptionDetail.planAmountUnit();
		BigDecimal actualPayment = memberSubscriptionDetail.planAmount();
		if (dutchPay) {
			actualPayment = memberSubscriptionDetail.dutchPayAmount();
		}
		if (!dutchPay && amountUnit.equals(SubscriptionAmountUnit.USD)) {
			actualPayment = actualPayment.multiply(memberSubscriptionDetail.exchangeRate())
				.setScale(0, RoundingMode.HALF_UP);
		}

		List<SpendingRecordSummary> spendingRecords = loadSpendingRecordPort.loadRecent3ByMemberIdAndSubscriptionName(
				memberId,
				memberSubscriptionDetail.subscriptionName()
			).stream()
			.map(SpendingRecordSummary::fromDomain)
			.toList();

		return ReadMemberSubscriptionResponse.from(
			memberSubscriptionDetail,
			now,
			paymentProgressPercent,
			actualPayment,
			spendingRecords
		);
	}
}
