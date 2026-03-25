package subport.batch.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.member.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		FROM Member m
		WHERE m.role = subport.domain.member.MemberRole.GUEST
		AND m.createdAt < :end
		""")
	List<Member> getGuestsBefore(LocalDateTime end);
}