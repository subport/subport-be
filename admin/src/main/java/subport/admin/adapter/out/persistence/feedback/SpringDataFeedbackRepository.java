package subport.admin.adapter.out.persistence.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.feedback.Feedback;
import subport.domain.feedback.FeedbackCategory;

public interface SpringDataFeedbackRepository extends JpaRepository<Feedback, Long> {

	@Query("""
		SELECT f
		FROM Feedback f
		WHERE (:start IS NULL OR f.createdAt >= :start)
		AND (:end IS NULL OR f.createdAt < :end)
		AND (:category IS NULL OR f.category = :category)
		""")
	Page<Feedback> findFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		FeedbackCategory category,
		Pageable pageable
	);
}
