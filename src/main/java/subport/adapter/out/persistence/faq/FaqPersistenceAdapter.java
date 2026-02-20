package subport.adapter.out.persistence.faq;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.faq.port.out.LoadFaqPort;
import subport.domain.faq.Faq;

@Component
@RequiredArgsConstructor
public class FaqPersistenceAdapter implements
	LoadFaqPort {

	private final SpringDataFaqRepository faqRepository;

	@Override
	public List<Faq> load() {
		return faqRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}
}
