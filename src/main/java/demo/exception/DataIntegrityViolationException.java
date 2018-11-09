package demo.exception;

import org.springframework.dao.NonTransientDataAccessException;

/**
 * Root of the hierarchy of data access exceptions that are considered non-transient
 * - where a retry of the same operation would fail unless the cause of the Exception is corrected.
 */
public class DataIntegrityViolationException extends NonTransientDataAccessException {
	public DataIntegrityViolationException(String msg) {
		super(msg);
	}

	public DataIntegrityViolationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
