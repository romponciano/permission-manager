package exception;

import java.io.Serializable;

public class DeleteException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DeleteException() { super(); }

	public DeleteException(String msg) { super(msg); }

	public DeleteException(String msg, Throwable cause) { super(msg, cause); }

	public DeleteException(Throwable cause) { super(cause); }
}
