package subport.domain.spendingrecord;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;

@Entity
@Table(name = "spending_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpendingRecord extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate paymentDate;

	private BigDecimal amount;

	private int durationMonths;

	private String subscriptionName;

	private String subscriptionLogoImageUrl;

	private Long memberId;

	private Long memberSubscriptionId;

	public SpendingRecord(
		LocalDate paymentDate,
		BigDecimal amount,
		int durationMonths,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		Long memberId,
		Long memberSubscriptionId
	) {
		this.paymentDate = paymentDate;
		this.amount = amount;
		this.durationMonths = durationMonths;
		this.subscriptionName = subscriptionName;
		this.subscriptionLogoImageUrl = subscriptionLogoImageUrl;
		this.memberId = memberId;
		this.memberSubscriptionId = memberSubscriptionId;
	}
}
