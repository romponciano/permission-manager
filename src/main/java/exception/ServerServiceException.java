package exception;

import java.io.Serializable;

public class ServerServiceException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 5168793577135085641L;

	public ServerServiceException() { super(); }

	public ServerServiceException(String msg) { super(msg); }

	public ServerServiceException(String msg, Throwable cause) { super(msg, cause); }

	public ServerServiceException(Throwable cause) { super(cause); }
}
