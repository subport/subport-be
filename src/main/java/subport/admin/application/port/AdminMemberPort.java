package subport.admin.application.port;

import java.time.LocalDateTime;

public interface AdminMemberPort {

	long countMembers();

	long countMembers(LocalDateTime start, LocalDateTime end);

	long countActiveMembers(LocalDateTime start, LocalDateTime end);
}
