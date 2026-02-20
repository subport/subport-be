package subport.adapter.in.web.faq;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.application.faq.port.in.FaqQueryUseCase;
import subport.application.faq.port.in.dto.GetFaqsResponse;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FaqController {

	private final FaqQueryUseCase faqQueryUseCase;

	@GetMapping
	public ResponseEntity<GetFaqsResponse> getFaqs() {
		return ResponseEntity.ok(faqQueryUseCase.getFaqs());
	}
}
