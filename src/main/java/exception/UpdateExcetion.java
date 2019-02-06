package exception;

import java.io.Serializable;

public class UpdateExcetion extends Exception implements Serializable {
	private static final long serialVersionUID = -6154515410619568364L;

	public UpdateExcetion() { super(); }

	public UpdateExcetion(String msg) { super(msg); }

	public UpdateExcetion(String msg, Throwable cause) { super(msg, cause); }

	public UpdateExcetion(Throwable cause) { super(cause); }
}
