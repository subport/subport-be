package subport.admin.application.port;

import java.util.List;

import org.springframework.data.domain.Sort;

import subport.domain.faq.Faq;

public interface AdminFaqPort {

	void saveFaq(Faq faq);

	Faq load(Long id);

	List<Faq> load(Sort sort);
}
