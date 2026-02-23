package subport.admin.application.faq;

import java.util.List;

import org.springframework.data.domain.Sort;

import subport.domain.faq.Faq;

public interface FaqPort {

	void saveFaq(Faq faq);

	Faq load(Long id);

	List<Faq> load(Sort sort);

	void deleteFaq(Long id);
}
