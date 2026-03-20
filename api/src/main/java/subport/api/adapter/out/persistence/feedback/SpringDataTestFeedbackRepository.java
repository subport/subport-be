package subport.api.adapter.out.persistence.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.feedback.TestFeedback;

public interface SpringDataTestFeedbackRepository extends JpaRepository<TestFeedback, Long> {
}
