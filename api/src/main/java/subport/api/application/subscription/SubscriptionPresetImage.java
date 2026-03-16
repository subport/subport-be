package subport.api.application.subscription;

import lombok.Getter;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;

@Getter
public enum SubscriptionPresetImage {

	ART("art"),
	BOOKS("books"),
	BOX("box"),
	CARD("card"),
	CLOUD("cloud"),
	EXERCISE("exercise"),
	GAME("game"),
	MOBILE("mobile"),
	MUSIC("music"),
	NEWS("news"),
	ROBOT("robot"),
	TV("tv"),
	WEB("web");

	private static final String BASE_URL = "https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/";

	private final String imageName;

	SubscriptionPresetImage(String imageName) {
		this.imageName = imageName;
	}

	public String getUrl() {
		return BASE_URL + "preset_" + imageName + ".png";
	}

	public static SubscriptionPresetImage fromName(String name) {
		for (SubscriptionPresetImage image : values()) {
			if (image.imageName.equals(name)) {
				return image;
			}
		}
		throw new CustomException(ApiErrorCode.INVALID_DEFAULT_IMAGE_NAME);
	}

	public static boolean isPresetImage(String url) {
		for (SubscriptionPresetImage image : values()) {
			if (image.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}
}
