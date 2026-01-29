package subport.application.member.port.in;

import java.time.LocalDate;

import subport.application.member.port.in.dto.GetMemberProfileResponse;

public interface GetMemberProfileUseCase {

	GetMemberProfileResponse get(Long memberId, LocalDate currentDate);
}
