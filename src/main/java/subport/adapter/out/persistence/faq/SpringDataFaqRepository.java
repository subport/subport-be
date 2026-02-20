package subport.adapter.out.persistence.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.faq.Faq;

public interface SpringDataFaqRepository extends JpaRepository<Faq, Long> {
}
