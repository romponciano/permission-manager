package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public final class Const implements Serializable {
	private static final long serialVersionUID = -8903464800683508311L;
	
	// JDBC driver name and database URL 
    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DB_URL = "jdbc:oracle:thin:@localhost:32118/XE";
    public static final String DB_TEST_URL = "jdbc:oracle:thin:@localhost:32119/XE"; 
    
    // Database credentials 
    public static final String USER = "ROMULOPONCIANO"; 
    public static final String PASS = "root";
    public static final String USER_TEST = "ROMULOTEST"; 
    public static final String PASS_TEST = "root";
    
    // formato de data para models e DB
    public static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    // Auto messages
    public static final String ERROR_REMOTE_EXCEPT = "Erro ao tentar realizar comunicação com o servidor";
    public static final String ERROR_DB_CONNECT = "Erro ao tentar conectar com o banco de dados.";
    public static final String ERROR_DB_CONSULT = "Erro ao tentar consultar ?1 no banco de dados.";
    public static final String ERROR_NOTBOUND_EXCEPT = "Erro ao renderizar interface.";
	public static final String ERROR_DB_CREATE = "Erro ao tentar realizar o cadastro de ?1.";
	public static final String ERROR_DB_UPDATE = "Erro ao tentar atualizar ?1.";
	public static final String ERROR_DB_DELETE = "Erro ao tentar remover ?1.";

	public static final String INFO_DATA_NOT_FOUND = "Nenhum dado encontrado.";
	public static final String INFO_EMPTY_FIELD = "O campo ?1 não pode ser vazio.";
	public static final String INFO_BIG_FIELD = "O campo ?1 não pode ter mais de ?2 caracters";

	public static final String WARN_CONFIRM_DELETE = "Você tem certeza que deseja excluir ?1 ?";
	
	/**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	 */
	private Const(){
		throw new AssertionError();
	};
}
