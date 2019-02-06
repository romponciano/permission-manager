package exception;

import java.io.Serializable;

public class CreateException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public CreateException() { super(); }

	public CreateException(String msg) { super(msg); }

	public CreateException(String msg, Throwable cause) { super(msg, cause); }

	public CreateException(Throwable cause) { super(cause); }
}
