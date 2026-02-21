package subport.application.faq.port.out;

import java.util.List;

import org.springframework.data.domain.Sort;

import subport.domain.faq.Faq;

public interface LoadFaqPort {

	List<Faq> load(Sort sort);
}
