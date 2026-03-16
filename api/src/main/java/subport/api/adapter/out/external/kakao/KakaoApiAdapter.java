package subport.api.adapter.out.external.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import subport.api.application.member.port.out.UnlinkMemberPort;

@Component
@RequiredArgsConstructor
public class KakaoApiAdapter implements UnlinkMemberPort {

	private final RestClient kakaoClient;

	@Value("${app.kakao.admin-key}")
	private String adminKey;

	@Override
	public void unlink(String providerId) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("target_id_type", "user_id");
		formData.add("target_id", providerId);

		kakaoClient.post()
			.uri("/unlink")
			.header("Authorization", "KakaoAK " + adminKey)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(formData)
			.retrieve()
			.toBodilessEntity();
	}
}
