package common;

import java.io.Serializable;

public final class Const implements Serializable {
	private static final long serialVersionUID = -8903464800683508311L;
	
	// JDBC driver name and database URL 
    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XE";  
    
    // Database credentials 
    public static final String USER = "ROMULOPONCIANO"; 
    public static final String PASS = "root";
	
	/**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	 */
	private Const(){
		throw new AssertionError();
	};
}
