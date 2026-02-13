package subport.admin.application.port;

import java.time.LocalDateTime;
import java.util.List;

import subport.domain.member.Member;

public interface AdminMemberPort {

	List<Member> loadMembers(LocalDateTime start, LocalDateTime end);

	long countMembers();

	long countMembers(LocalDateTime start, LocalDateTime end);

	long countActiveMembers(LocalDateTime start, LocalDateTime end);
}
