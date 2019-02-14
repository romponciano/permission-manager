package exception;

import java.io.Serializable;

public class DBUpdateException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DBUpdateException() { super(); }

	public DBUpdateException(String msg) { super(msg); }

	public DBUpdateException(String msg, Throwable cause) { super(msg, cause); }

	public DBUpdateException(Throwable cause) { super(cause); }
}
