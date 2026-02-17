package subport.admin.application.port;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.domain.member.Member;

public interface AdminMemberPort {

	List<Member> loadMembers(LocalDateTime start, LocalDateTime end);

	List<Member> loadLatestMembers();

	long countMembers();

	long countMembers(LocalDateTime start, LocalDateTime end);

	long countActiveMembers(LocalDateTime start, LocalDateTime end);

	Page<Member> searchMembers(
		Boolean deleted,
		Boolean reminderEnabled,
		String email,
		Pageable pageable
	);
}
