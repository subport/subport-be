package subport.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientUrlConstants {

	public static final String LOCAL_HTTP_ORIGIN = "http://localhost:5173";
	public static final String LOCAL_HTTPS_ORIGIN = "https://localhost:5173";
	public static final String PROD_ORIGIN = "https://subport.site";

	public static final String LOGIN_SUCCESS_PATH = "/login-success";
}
