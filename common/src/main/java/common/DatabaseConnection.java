package common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import common.exceptions.DBConnectException;

public class DatabaseConnection implements Serializable {
	private static final long serialVersionUID = 6153988507264596623L;

    private Connection connection;
    private String DBurl;
    private String DBuser;
    private String DBpass;
    
    public DatabaseConnection() throws DBConnectException {
    	try {
    		Class.forName(Const.JDBC_DRIVER);
    		this.DBurl = Const.DB_URL;
    		this.DBuser = Const.USER;
    		this.DBpass = Const.PASS;
    	} catch (Exception e) {
    		throw new DBConnectException(e.getMessage(), e.getCause());
    	}
    };
    
    public DatabaseConnection(String dburl, String dbuser, String dbpass) throws DBConnectException {
    	try {
    		Class.forName(Const.JDBC_DRIVER);
    		this.DBurl = dburl;
    		this.DBuser = dbuser;
    		this.DBpass = dbpass;
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
				this.connection = DriverManager.getConnection(DBurl, DBuser, DBpass);
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
