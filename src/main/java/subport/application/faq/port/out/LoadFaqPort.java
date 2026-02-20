package subport.application.faq.port.out;

import java.util.List;

import subport.domain.faq.Faq;

public interface LoadFaqPort {

	List<Faq> load();
}
