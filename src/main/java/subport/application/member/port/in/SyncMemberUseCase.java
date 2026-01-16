package subport.application.member.port.in;

import subport.application.member.port.in.dto.LoginMemberInfo;

public interface SyncMemberUseCase {

	Long sync(LoginMemberInfo loginMemberInfo);
}
