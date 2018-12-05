package demo.exception;

public class NoRolesFoundException extends Exception {

	public NoRolesFoundException(String message) {
		super(message);
	}

	public NoRolesFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
