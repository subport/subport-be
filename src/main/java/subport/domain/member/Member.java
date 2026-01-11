package subport.domain.member;

import lombok.Getter;

@Getter
public class Member {

	private Long id;

	private String providerId;

	private String nickname;

	private String profileImageUrl;

	private String email;

	private Member(
		Long id,
		String providerId,
		String nickname,
		String profileImageUrl,
		String email
	) {
		this.id = id;
		this.providerId = providerId;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.email = email;
	}

	public static Member withId(
		Long id,
		String providerId,
		String nickname,
		String profileImageUrl,
		String email
	) {
		return new Member(
			id,
			providerId,
			nickname,
			profileImageUrl,
			email
		);
	}

	public static Member withoutId(
		String providerId,
		String nickname,
		String profileImageUrl,
		String email
	) {
		return new Member(
			null,
			providerId,
			nickname,
			profileImageUrl,
			email
		);
	}
}
