package subport.batch.persistence;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import subport.domain.member.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT COUNT(m)
		FROM Member m
		WHERE m.role = subport.domain.member.MemberRole.GUEST
		AND m.createdAt >= :start AND m.createdAt < :end
		""")
	int countYesterdayGuests(LocalDateTime start, LocalDateTime end);

	@Modifying
	@Query("""
		DELETE FROM Member m
		WHERE m.role = subport.domain.member.MemberRole.GUEST
		AND m.createdAt < :end
		""")
	int deleteGuestsBefore(LocalDateTime end);
}