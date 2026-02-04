package subport.domain.subscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.domain.member.Member;

@Entity
@Table(name = "plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Plan extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal amount;

	@Enumerated(value = EnumType.STRING)
	private AmountUnit amountUnit;

	private int durationMonths;

	private boolean systemProvided;

	@JoinColumn(name = "member_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "subscription_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Subscription subscription;

	public Plan(
		String name,
		BigDecimal amount,
		AmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Member member,
		Subscription subscription
	) {
		this.name = name;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
		this.systemProvided = systemProvided;
		this.member = member;
		this.subscription = subscription;
	}

	public void update(
		String name,
		BigDecimal amount,
		AmountUnit amountUnit,
		int durationMonths
	) {
		this.name = name;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
	}

	public LocalDate calculateNextPaymentDate(LocalDate date) {
		return date.plusMonths(durationMonths);
	}

	public boolean isUsdBased() {
		return this.amountUnit.equals(AmountUnit.USD);
	}
}
