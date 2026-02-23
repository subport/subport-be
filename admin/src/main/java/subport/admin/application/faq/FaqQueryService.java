package subport.admin.application.faq;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.faq.dto.FaqResponse;
import subport.admin.application.faq.dto.FaqsResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FaqQueryService {

	private final FaqPort faqPort;

	public FaqsResponse getFaqs() {
		Sort sort = Sort.by(Sort.Direction.ASC, "id");

		return new FaqsResponse(
			faqPort.load(sort).stream()
				.map(FaqResponse::from)
				.toList()
		);
	}

}
