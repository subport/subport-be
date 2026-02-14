package subport.domain.subscription;

import java.text.Normalizer;
import java.util.Locale;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.domain.member.Member;

@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Subscription extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String normalizedName;

	@Enumerated(value = EnumType.STRING)
	private SubscriptionType type;

	private String logoImageUrl;

	private String planUrl;

	private boolean systemProvided;

	@JoinColumn(name = "member_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public Subscription(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		boolean systemProvided,
		Member member
	) {
		this.name = name;
		this.type = type;
		this.logoImageUrl = logoImageUrl;
		this.planUrl = planUrl;
		this.systemProvided = systemProvided;
		this.member = member;
	}

	public void update(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl
	) {
		this.name = name;
		this.type = type;
		this.logoImageUrl = logoImageUrl;
		this.planUrl = planUrl;
	}

	@PrePersist
	@PreUpdate
	private void syncNormalizedName() {
		this.normalizedName = normalize(this.name);
	}

	private String normalize(String name) {
		String normalized = Normalizer.normalize(name, Normalizer.Form.NFKC);

		return normalized.toLowerCase(Locale.ROOT)
			.replaceAll("\\s+", "")
			.replaceAll("[^a-z0-9가-힣]", "");
	}
}
