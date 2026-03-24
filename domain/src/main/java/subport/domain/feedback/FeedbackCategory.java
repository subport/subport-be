package subport.domain.feedback;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FeedbackCategory {

	IMPROVEMENT("기능 개선"),
	BUG("버그/오류"),
	INCONVENIENCE("사용성 불편"),
	OTHER("기타");

	private final String displayName;

	public static FeedbackCategory from(String displayName) {
		for (FeedbackCategory category : values()) {
			if (category.displayName.equals(displayName)) {
				return category;
			}
		}
		return null; // 도메인 예외로 수정 예정
	}
}
