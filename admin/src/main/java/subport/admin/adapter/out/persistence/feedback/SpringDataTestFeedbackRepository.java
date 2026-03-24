package subport.admin.adapter.out.persistence.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.feedback.TestFeedback;

public interface SpringDataTestFeedbackRepository extends JpaRepository<TestFeedback, Long> {

	@Query("""
		SELECT tf
		FROM TestFeedback tf
		WHERE (:start IS NULL OR tf.createdAt >= :start)
		AND (:end IS NULL OR tf.createdAt < :end)
		""")
	Page<TestFeedback> findTestFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	);
}
