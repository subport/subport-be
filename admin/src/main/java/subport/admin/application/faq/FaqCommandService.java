package subport.admin.application.faq;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.faq.dto.UpdateFaqRequest;
import subport.admin.application.faq.dto.WriteFaqRequest;
import subport.domain.faq.Faq;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqCommandService {

	private final FaqPort faqPort;

	public void writeFaq(WriteFaqRequest request) {
		faqPort.saveFaq(
			new Faq(
				request.question(),
				request.answer()
			)
		);
	}

	public void updateFaq(UpdateFaqRequest request, Long faqId) {
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
