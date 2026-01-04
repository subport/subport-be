package subport.domain.subscription;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class Subscription {

	private final Long id;

	private final String name;

	private final Long typeId;

	private final Long planId;

	private final int headCount;

	private final LocalDate startAt;

	private final LocalDate endAt;

	private final boolean isActive;

	private final String memo;

	private final Long memberId;

	private Subscription(
		Long id,
		String name,
		Long typeId,
		Long planId,
		int headCount,
		LocalDate startAt,
		LocalDate endAt,
		String memo,
		Long memberId
	) {
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.planId = planId;
		this.headCount = headCount;
		this.startAt = startAt;
		this.endAt = endAt;
		this.isActive = true;
		this.memo = memo;
		this.memberId = memberId;
	}

	public static Subscription withId(
		Long id,
		String name,
		Long typeId,
		Long planId,
		int headCount,
		LocalDate startAt,
		LocalDate endAt,
		String memo,
		Long memberId
	) {
		return new Subscription(
			id,
			name,
			typeId,
			planId,
			headCount,
			startAt,
			endAt,
			memo,
			memberId
		);
	}

	public static Subscription withoutId(
		String name,
		Long typeId,
		Long planId,
		int headCount,
		LocalDate startAt,
		LocalDate endAt,
		String memo,
		Long memberId
	) {
		return new Subscription(
			null,
			name,
			typeId,
			planId,
			headCount,
			startAt,
			endAt,
			memo,
			memberId
		);
	}
}