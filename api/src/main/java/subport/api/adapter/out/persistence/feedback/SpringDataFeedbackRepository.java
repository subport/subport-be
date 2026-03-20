package subport.api.adapter.out.persistence.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.feedback.Feedback;

public interface SpringDataFeedbackRepository extends JpaRepository<Feedback, Long> {
}
