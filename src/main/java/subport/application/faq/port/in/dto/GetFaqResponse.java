package subport.application.faq.port.in.dto;

import subport.domain.faq.Faq;

public record GetFaqResponse(
	Long id,
	String question,
	String answer
) {

	public static GetFaqResponse from(Faq faq) {
		return new GetFaqResponse(
			faq.getId(),
			faq.getQuestion(),
			faq.getAnswer()
		);
	}
}
