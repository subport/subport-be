package subport.api.application.subscription;

import lombok.Getter;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;

@Getter
public enum SubscriptionDefaultImage {

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

	SubscriptionDefaultImage(String imageName) {
		this.imageName = imageName;
	}

	public String getUrl() {
		return BASE_URL + "default_" + imageName + ".png";
	}

	public static SubscriptionDefaultImage fromName(String name) {
		for (SubscriptionDefaultImage image : values()) {
			if (image.imageName.equals(name)) {
				return image;
			}
		}
		throw new CustomException(ApiErrorCode.INVALID_DEFAULT_IMAGE_NAME);
	}
}
