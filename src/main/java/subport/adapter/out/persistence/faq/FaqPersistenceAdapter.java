package subport.adapter.out.persistence.faq;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminFaqPort;
import subport.application.faq.port.out.LoadFaqPort;
import subport.domain.faq.Faq;

@Component
@RequiredArgsConstructor
public class FaqPersistenceAdapter implements
	LoadFaqPort,
	AdminFaqPort {

	private final SpringDataFaqRepository faqRepository;

	@Override
	public void saveFaq(Faq faq) {
		faqRepository.save(faq);
	}

	@Override
	public List<Faq> load(Sort sort) {
		return faqRepository.findAll(sort);
	}
}
