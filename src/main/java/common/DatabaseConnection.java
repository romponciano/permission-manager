package common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection implements Serializable {
	private static final long serialVersionUID = 6153988507264596623L;
	
	// vars
    Connection connection; 
    Statement statement;
    
    /**
     * Ao criar uma instância desta classe, você esta 
     * abrindo uma conexão com o banco. 
     * @throws ClassNotFoundException se erro ao registrar JDBC
     * @throws SQLException se erro ao criar conexão com o banco
     */
    public DatabaseConnection() throws ClassNotFoundException, SQLException {
    	// registrar driver JDBC
    	Class.forName(Const.JDBC_DRIVER);
    	// abrir conexão
		this.connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
    	this.statement = null;
    };
    
    /**
     * pegar conexão da instância conectada  
     */
    public Connection getConnection() {
    	return this.connection;
    }
    
    
    
    /**
     * Método para pegar statement da instância de conexão 
     * @return novo statement (se null) ou statement corrente
     * @throws SQLException se erro ao criar novo statement
     */
    public Statement getStatement() throws SQLException {
    	if(this.statement == null) {
    		this.statement = this.connection.createStatement();
    	};
    	return statement;
    };
    
    /**
     * Método para fechar statement corrente da instância de conexão
     * @throws SQLException se erro ao fechar statement não nulo
     */
    public void closeStatement() throws SQLException {
    	if (this.statement != null) {
            this.statement.close();        
            this.statement = null;
        };
    };
    
}
