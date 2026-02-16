package subport.adapter.out.persistence.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.member.Member;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByProviderId(String providerId);

	@Query("""
		SELECT m
		FROM Member m
		WHERE (m.createdAt >= :start AND m.createdAt < :end)
		AND m.deleted = false
		""")
	List<Member> getMembers(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE m.deleted = false
		""")
	long countMembers();

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE (m.createdAt >= :start AND m.createdAt < :end)
		AND m.deleted = false
		""")
	long countMembers(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE (
			(m.lastLoginAt >= :start AND m.lastLoginAt < :end)
			OR
			(m.lastModifiedAt >= :start AND m.lastModifiedAt < :end)
		)
		AND m.deleted = false
		""")
	long countActiveMembers(LocalDateTime start, LocalDateTime end);

	List<Member> findTop4ByOrderByCreatedAtDesc();
}
