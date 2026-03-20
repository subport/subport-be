package subport.api.application.auth.service;

import java.time.Instant;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.in.GuestLoginUseCase;
import subport.api.application.auth.port.out.CreateAccessTokenPort;
import subport.api.application.member.port.out.SaveMemberPort;
import subport.common.jwt.dto.AccessTokenResponse;
import subport.domain.member.Member;
import subport.domain.member.MemberRole;

@Service
@Transactional
@RequiredArgsConstructor
public class GuestLoginService implements GuestLoginUseCase {

	private final SaveMemberPort saveMemberPort;
	private final CreateAccessTokenPort createAccessTokenPort;

	@Override
	public AccessTokenResponse login(Instant now) {
		Member guest = Member.ofGuest(generateGuestNickname());
		Long guestId = saveMemberPort.save(guest);

		return new AccessTokenResponse(
			createAccessTokenPort.createAccessToken(guestId, now, MemberRole.GUEST)
		);
	}

	private String generateGuestNickname() {
		String random6 = new Random().ints(6, 0, 36)
			.mapToObj(n -> Integer.toString(n, 36))
			.collect(Collectors.joining());
		return "guest_" + random6;
	}
}
