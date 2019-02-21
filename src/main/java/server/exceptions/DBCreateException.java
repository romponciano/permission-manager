package server.exceptions;

import java.io.Serializable;

public class DBCreateException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DBCreateException() { super(); }

	public DBCreateException(String msg) { super(msg); }

	public DBCreateException(String msg, Throwable cause) { super(msg, cause); }

	public DBCreateException(Throwable cause) { super(cause); }
}
