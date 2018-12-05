package demo.exception;

public class UnauthorizedRequestException extends Exception {

	public UnauthorizedRequestException(String message) {
		super(message);
	}

	public UnauthorizedRequestException(String message, Throwable t) {
		super(message, t);
	}
}
