package subport.admin.application.dto;

public record AdminUpdateFaqRequest(
	String question,
	String answer
) {
}
