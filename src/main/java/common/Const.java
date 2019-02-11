package common;

import java.io.Serializable;

public final class Const implements Serializable {
	private static final long serialVersionUID = -8903464800683508311L;
	
	// JDBC driver name and database URL 
    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DB_URL = "jdbc:oracle:thin:@localhost:32118/XE";  
    
    // Database credentials 
    public static final String USER = "ROMULOPONCIANO"; 
    public static final String PASS = "root";
    
    // Auto messages
    public static final String ERROR_REMOTE_EXCEPT = "Erro ao tentar realizar comunicação com o servidor";
    public static final String ERROR_DB_CONNECT = "Erro ao tentar conectar com o banco de dados.";
    public static final String ERROR_DB_CONSULT = "Erro ao tentar consultar o banco de dados.";
    public static final String ERROR_NOTBOUND_EXCEPT = "Erro ao renderizar interface.";
    public static final String INFO_DATA_NOT_FOUND = "Nenhum dado encontrado.";
	
	/**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	 */
	private Const(){
		throw new AssertionError();
	};
}
