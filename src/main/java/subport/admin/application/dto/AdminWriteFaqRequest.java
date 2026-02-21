package subport.admin.application.dto;

public record AdminWriteFaqRequest(
	String question,
	String answer
) {
}
