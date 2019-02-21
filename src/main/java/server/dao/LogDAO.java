package server.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;

import common.DatabaseConnection;
import server.utils.Log;

public class LogDAO implements Serializable {
	
	private static final long serialVersionUID = 2471323214818735602L;
	
	public void createLog(Log log) {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("INSERT INTO LOG (tipo, mensagem, causa) VALUES (?,?,?)");
			statment.setString(1, log.getTipo());
			statment.setString(2, log.getMessage());
			statment.setString(3, log.getCausa());
			statment.executeUpdate();
		} catch (Exception e) {
			 // Ignorar pq qualquer exceção ao inserir o Log, não tem o que ser feito
		} finally {
		    try { statment.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
	
}
