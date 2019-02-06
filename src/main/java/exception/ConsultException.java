package exception;

import java.io.Serializable;

public class ConsultException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public ConsultException() { super(); }

	public ConsultException(String msg) { super(msg); }

	public ConsultException(String msg, Throwable cause) { super(msg, cause); }

	public ConsultException(Throwable cause) { super(cause); }
}
