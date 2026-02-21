package subport.admin.application.dto;

import java.time.LocalDateTime;

import subport.domain.faq.Faq;

public record AdminFaqResponse(
	Long id,
	String question,
	String answer,
	LocalDateTime createdAt,
	LocalDateTime lastModifiedAt
) {

	public static AdminFaqResponse from(Faq faq) {
		return new AdminFaqResponse(
			faq.getId(),
			faq.getQuestion(),
			faq.getAnswer(),
			faq.getCreatedAt(),
			faq.getLastModifiedAt()
		);
	}
}
