package subport.admin.application.faq.dto;

import java.time.LocalDateTime;

import subport.domain.faq.Faq;

public record FaqResponse(
	Long id,
	String question,
	String answer,
	LocalDateTime createdAt,
	LocalDateTime lastModifiedAt
) {

	public static FaqResponse from(Faq faq) {
		return new FaqResponse(
			faq.getId(),
			faq.getQuestion(),
			faq.getAnswer(),
			faq.getCreatedAt(),
			faq.getLastModifiedAt()
		);
	}
}
