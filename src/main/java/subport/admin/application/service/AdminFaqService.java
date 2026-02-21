package subport.admin.application.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminFaqResponse;
import subport.admin.application.dto.AdminFaqsResponse;
import subport.admin.application.port.AdminFaqPort;

@Service
@RequiredArgsConstructor
public class AdminFaqService {

	private final AdminFaqPort faqPort;

	@Transactional(readOnly = true)
	public AdminFaqsResponse getFaqs() {
		Sort sort = Sort.by(Sort.Direction.DESC, "lastModifiedAt")
			.and(Sort.by(Sort.Direction.ASC, "id"));

		return new AdminFaqsResponse(
			faqPort.load(sort).stream()
				.map(AdminFaqResponse::from)
				.toList()
		);
	}
}
