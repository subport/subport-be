package subport.admin.application.faq.dto;

public record WriteFaqRequest(
	String question,
	String answer
) {
}
