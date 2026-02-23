package subport.admin.application.faq.dto;

public record UpdateFaqRequest(
	String question,
	String answer
) {
}
