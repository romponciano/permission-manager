package exception;

import java.io.Serializable;

public class ConnectionException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public ConnectionException() { super(); }

	public ConnectionException(String msg) { super(msg); }

	public ConnectionException(String msg, Throwable cause) { super(msg, cause); }

	public ConnectionException(Throwable cause) { super(cause); }
}
