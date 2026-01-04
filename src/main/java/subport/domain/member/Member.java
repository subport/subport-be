package subport.domain.member;

import lombok.Getter;

@Getter
public class Member {

	private Long id;

	private String providerId;

	private String nickname;

	private String email;

	private Member(
		Long id,
		String providerId,
		String nickname,
		String email
	) {
		this.id = id;
		this.providerId = providerId;
		this.nickname = nickname;
		this.email = email;
	}

	public static Member withId(
		Long id,
		String providerId,
		String nickname,
		String email
	) {
		return new Member(
			id,
			providerId,
			nickname,
			email
		);
	}

	public static Member withoutId(
		String providerId,
		String nickname,
		String email
	) {
		return new Member(
			null,
			providerId,
			nickname,
			email
		);
	}
}
