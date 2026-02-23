package subport.admin.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.faq.FaqCommandService;
import subport.admin.application.faq.FaqQueryService;
import subport.admin.application.faq.dto.FaqsResponse;
import subport.admin.application.faq.dto.UpdateFaqRequest;
import subport.admin.application.faq.dto.WriteFaqRequest;

@RestController
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class FaqController {

	private final FaqCommandService faqCommandService;
	private final FaqQueryService faqQueryService;

	@PostMapping
	public ResponseEntity<Void> writeFaq(@RequestBody WriteFaqRequest request) {
		faqCommandService.writeFaq(request);

		return ResponseEntity.ok()
			.build();
	}

	@GetMapping
	public ResponseEntity<FaqsResponse> getFaqs() {
		return ResponseEntity.ok(
			faqQueryService.getFaqs()
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateFaq(
		@RequestBody UpdateFaqRequest request,
		@PathVariable("id") Long faqId
	) {
		faqCommandService.updateFaq(request, faqId);

		return ResponseEntity.noContent()
			.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFaq(@PathVariable("id") Long faqId) {
		faqCommandService.deleteFaq(faqId);

		return ResponseEntity.noContent()
			.build();
	}
}
