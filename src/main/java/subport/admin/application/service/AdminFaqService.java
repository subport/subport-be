package subport.admin.application.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminFaqResponse;
import subport.admin.application.dto.AdminFaqsResponse;
import subport.admin.application.dto.AdminUpdateFaqRequest;
import subport.admin.application.dto.AdminWriteFaqRequest;
import subport.admin.application.port.AdminFaqPort;
import subport.domain.faq.Faq;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminFaqService {

	private final AdminFaqPort faqPort;

	public void writeFaq(AdminWriteFaqRequest request) {
		faqPort.saveFaq(
			new Faq(
				request.question(),
				request.answer()
			)
		);
	}

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

	public void updateFaq(AdminUpdateFaqRequest request, Long faqId) {
		Faq faq = faqPort.load(faqId);

		if (faq == null) {
			return;
		}

		faq.update(
			request.question(),
			request.answer()
		);
	}

	public void deleteFaq(Long faqId) {
		faqPort.deleteFaq(faqId);
	}
}
