package subport.application.member.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.GetMemberProfileUseCase;
import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMemberProfileService implements GetMemberProfileUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public GetMemberProfileResponse get(Long memberId, LocalDate currentDate) {
		Member member = loadMemberPort.load(memberId);

		return new GetMemberProfileResponse(
			member.getNickname(),
			ChronoUnit.DAYS.between(
				member.getCreatedAt().toLocalDate(),
				currentDate
			)
		);
	}
}
