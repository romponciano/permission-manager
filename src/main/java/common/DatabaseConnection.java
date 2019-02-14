package common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DBConnectException;

public class DatabaseConnection implements Serializable {
	private static final long serialVersionUID = 6153988507264596623L;

    private Connection connection;
    
    /**
     * Ao criar uma instância desta classe, você esta 
     * tentando abrir uma NOVA conexão com o banco. 
     * @throws DBConnectException se falhar ao tentar conexão ou registro do driver
     */
    public DatabaseConnection() throws DBConnectException {
    	try {
    		Class.forName(Const.JDBC_DRIVER);
        	this.getConnection();
    	} catch (Exception e) {
    		throw new DBConnectException(e.getMessage(), e.getCause());
    	}
    };
    
    /**
     * pegar conexão da instância conectada ou criar nova conexão, se não existir 
     * @throws DBConnectException se falhar ao tentar conexão ou registro do driver
     */
    public Connection getConnection() throws DBConnectException {
    	if(this.connection == null) {
    		try {
				this.connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
			} catch (SQLException e) {
				throw new DBConnectException(e.getMessage(), e.getCause());
			}
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
    	} catch (Exception e) { /* ignorar */ }
    }
    
}
