package server.exceptions;

import java.io.Serializable;

public class DBConnectException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DBConnectException() { super(); }

	public DBConnectException(String msg) { super(msg); }

	public DBConnectException(String msg, Throwable cause) { super(msg, cause); }

	public DBConnectException(Throwable cause) { super(cause); }
}
