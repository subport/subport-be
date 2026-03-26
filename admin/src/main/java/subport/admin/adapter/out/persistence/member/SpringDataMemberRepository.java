package subport.admin.adapter.out.persistence.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.member.Member;
import subport.domain.member.MemberRole;

public interface SpringDataMemberRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		FROM Member m
		WHERE (m.createdAt >= :start AND m.createdAt < :end)
		AND m.deleted = false
		AND m.role = subport.domain.member.MemberRole.MEMBER
		""")
	List<Member> findMembers(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT m
		FROM Member m
		WHERE m.role = subport.domain.member.MemberRole.MEMBER
		ORDER BY m.createdAt DESC, m.id ASC
		""")
	List<Member> findRecentMembers(Pageable pageable);

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE m.deleted = false
		AND m.role = subport.domain.member.MemberRole.MEMBER
		""")
	long countMembers();

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE (m.createdAt >= :start AND m.createdAt < :end)
		AND m.deleted = false
		AND m.role = :role
		""")
	long countMembers(LocalDateTime start, LocalDateTime end, MemberRole role);

	@Query("""
		SELECT count(m)
		FROM Member m
		WHERE (
			(m.lastActiveAt >= :start AND m.lastActiveAt < :end)
			OR
			(m.lastModifiedAt >= :start AND m.lastModifiedAt < :end)
		)
		AND m.deleted = false
		AND m.role = subport.domain.member.MemberRole.MEMBER
		""")
	long countActiveMembers(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT m
		FROM Member m
		WHERE (:deleted IS NULL OR m.deleted = :deleted)
		AND (m.role = subport.domain.member.MemberRole.MEMBER)
		AND (:reminderEnabled IS NULL OR m.paymentReminderEnabled = :reminderEnabled)
		AND (:email IS NULL OR m.email LIKE %:email%)
		""")
	Page<Member> findAllByFilter(
		Boolean deleted,
		Boolean reminderEnabled,
		String email,
		Pageable pageable
	);
}
