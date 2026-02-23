package subport.common.exception;

public interface ErrorCode {

	int getStatus();

	String getError();

	String getMessage();

	String getCode();
}
