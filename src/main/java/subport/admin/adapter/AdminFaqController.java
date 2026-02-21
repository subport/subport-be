package subport.admin.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminFaqsResponse;
import subport.admin.application.service.AdminFaqService;

@RestController
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class AdminFaqController {

	private final AdminFaqService faqService;

	@GetMapping
	public ResponseEntity<AdminFaqsResponse> getFaqs() {
		return ResponseEntity.ok(
			faqService.getFaqs()
		);
	}
}
