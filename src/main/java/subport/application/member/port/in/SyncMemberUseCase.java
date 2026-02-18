package subport.application.member.port.in;

import subport.application.member.port.in.dto.LoginMemberInfo;
import subport.application.member.port.in.dto.SyncMemberInfo;

public interface SyncMemberUseCase {

	SyncMemberInfo sync(LoginMemberInfo loginMemberInfo);
}
