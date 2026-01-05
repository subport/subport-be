package subport.domain.subscription;

import lombok.Getter;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Getter
public enum SubscriptionType {

	OTT("OTT"),
	MUSIC_STREAMING("음원 스트리밍"),
	VIDEO_STREAMING("동영상 스트리밍"),
	AI("AI"),
	SHOPPING("쇼핑"),
	FOOD("배달/음식"),
	BOOK("도서"),
	MESSENGER("메신저"),
	VPN("VPN"),
	CREATIVE_TOOL("크리에이티브 툴"),
	ETC("기타");

	private final String displayName;

	SubscriptionType(String displayName) {
		this.displayName = displayName;
	}

	public static SubscriptionType fromDisplayName(String displayName) {
		for (SubscriptionType type : SubscriptionType.values()) {
			if (type.displayName.equals(displayName)) {
				return type;
			}
		}
		throw new CustomException(ErrorCode.INVALID_SUBSCRIPTION_TYPE);
	}
}
