package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

import common.DatabaseConnection;
import exception.DBConnectException;
import exception.DBConsultException;
import model.Log;

public class LogDAO implements Serializable {
	
	private static final long serialVersionUID = 2471323214818735602L;

	public void createLog(Log log) throws DBConsultException, DBConnectException {
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery(
				"INSERT INTO LOG (id, tipo, mensagem, causa) VALUES " + 
				"(" + criarStringValores(log) + ")"
			);
		} catch (Exception e) {
			 // Ignorar pq qualquer exceção ao inserir o Log, não tem o que ser feito
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
	
	private String criarStringValores(Log log) {
		String saida = "";
		saida += log.getId() + ",";
		saida += log.getTipo() + ",";
		saida += log.getMessage() + ",";
		saida += log.getCausa();
		return saida;
	}
	
}
