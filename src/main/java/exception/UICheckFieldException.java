package exception;

import java.io.Serializable;

public class UICheckFieldException extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public UICheckFieldException() { super(); }

	public UICheckFieldException(String msg) { super(msg); }

	public UICheckFieldException(String msg, Throwable cause) { super(msg, cause); }

	public UICheckFieldException(Throwable cause) { super(cause); }
}
