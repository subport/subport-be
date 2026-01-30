package subport.application.member.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.MemberQueryUseCase;
import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService implements MemberQueryUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public GetMemberResponse getMember(Long memberId) {
		Member member = loadMemberPort.load(memberId);

		return GetMemberResponse.from(member);
	}

	@Override
	public GetMemberProfileResponse getMemberProfile(Long memberId, LocalDate currentDate) {
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
