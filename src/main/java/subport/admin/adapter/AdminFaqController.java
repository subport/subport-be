package subport.admin.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminFaqsResponse;
import subport.admin.application.dto.AdminUpdateFaqRequest;
import subport.admin.application.dto.AdminWriteFaqRequest;
import subport.admin.application.service.AdminFaqService;

@RestController
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class AdminFaqController {

	private final AdminFaqService faqService;

	@PostMapping
	public ResponseEntity<Void> writeFaq(@RequestBody AdminWriteFaqRequest request) {
		faqService.writeFaq(request);

		return ResponseEntity.ok()
			.build();
	}

	@GetMapping
	public ResponseEntity<AdminFaqsResponse> getFaqs() {
		return ResponseEntity.ok(
			faqService.getFaqs()
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateFaq(
		@RequestBody AdminUpdateFaqRequest request,
		@PathVariable("id") Long faqId
	) {
		faqService.updateFaq(request, faqId);

		return ResponseEntity.noContent()
			.build();
	}
}
