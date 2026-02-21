package subport.application.faq.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.faq.port.in.FaqQueryUseCase;
import subport.application.faq.port.in.dto.GetFaqResponse;
import subport.application.faq.port.in.dto.GetFaqsResponse;
import subport.application.faq.port.out.LoadFaqPort;

@Service
@RequiredArgsConstructor
public class FaqQueryService implements FaqQueryUseCase {

	private final LoadFaqPort loadFaqPort;

	@Override
	public GetFaqsResponse getFaqs() {
		Sort sort = Sort.by(Sort.Direction.ASC, "id");

		return new GetFaqsResponse(
			loadFaqPort.load(sort).stream()
				.map(GetFaqResponse::from)
				.toList()
		);
	}
}
