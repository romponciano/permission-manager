package server.exceptions;

import java.io.Serializable;

import common.Const;

public class DBDataNotFoundException extends Exception implements Serializable {
	
	private static final long serialVersionUID = -2354605809686343688L;

	public DBDataNotFoundException() { super(Const.INFO_DATA_NOT_FOUND); }

	public DBDataNotFoundException(String msg) { super(msg); }
}
