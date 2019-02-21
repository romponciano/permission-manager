package server.exceptions;

import java.io.Serializable;

public class DBDeleteException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DBDeleteException() { super(); }

	public DBDeleteException(String msg) { super(msg); }

	public DBDeleteException(String msg, Throwable cause) { super(msg, cause); }

	public DBDeleteException(Throwable cause) { super(cause); }
}
