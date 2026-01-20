package subport.application.membersubscription.port.in.dto;

public record ListMemberSubscriptionsRequest(
	boolean active,
	String sortBy
) {
}
