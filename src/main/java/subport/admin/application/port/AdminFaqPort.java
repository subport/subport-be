package subport.admin.application.port;

import java.util.List;

import org.springframework.data.domain.Sort;

import subport.domain.faq.Faq;

public interface AdminFaqPort {

	List<Faq> load(Sort sort);
}
