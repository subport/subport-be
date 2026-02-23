package subport.admin.application.account;

public interface PasswordEncoderPort {

	String encode(String rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}
