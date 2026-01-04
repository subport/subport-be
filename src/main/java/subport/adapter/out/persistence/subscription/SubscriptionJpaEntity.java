package subport.adapter.out.persistence.subscription;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.adapter.out.persistence.member.MemberJpaEntity;

@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionTypeJpaEntity type;

	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionPlanJpaEntity plan;

	private int headCount;

	private LocalDate startAt;

	private LocalDate endAt;

	private String memo;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	public SubscriptionJpaEntity(
		String name,
		SubscriptionTypeJpaEntity type,
		SubscriptionPlanJpaEntity plan,
		int headCount,
		LocalDate startAt,
		LocalDate endAt,
		String memo,
		MemberJpaEntity member
	) {
		this.name = name;
		this.type = type;
		this.plan = plan;
		this.headCount = headCount;
		this.startAt = startAt;
		this.endAt = endAt;
		this.memo = memo;
		this.member = member;
	}
}
