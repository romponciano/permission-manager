package server.exceptions;

import java.io.Serializable;

public class DBConsultException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public DBConsultException() { super(); }

	public DBConsultException(String msg) { super(msg); }

	public DBConsultException(String msg, Throwable cause) { super(msg, cause); }

	public DBConsultException(Throwable cause) { super(cause); }
}
