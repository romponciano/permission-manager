package common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements Serializable {
	private static final long serialVersionUID = 6153988507264596623L;
	
	// vars
    Connection connection;
    
    /**
     * Ao criar uma instância desta classe, você esta 
     * tentando abrir uma NOVA conexão com o banco. 
     * @throws ClassNotFoundException se erro ao registrar JDBC
     * @throws SQLException se erro ao criar conexão com o banco
     */
    public DatabaseConnection() throws ClassNotFoundException, SQLException {
    	// registrar driver JDBC
    	Class.forName(Const.JDBC_DRIVER);
    	// conectar
    	this.getConnection();
    };
    
    /**
     * pegar conexão da instância conectada ou criar nova conexão, se não existir 
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException {
    	if(this.connection == null) {
    		this.connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
    	}
    	return this.connection;
    }
    
    /**
     * fechar conexão da instância conectada e setar connection para null
     */
    public void closeConnection() {
    	try {
    		this.connection.close();
    		this.connection = null; 
    	} catch (Exception e) { /* ignored */ }
    }
    
}
